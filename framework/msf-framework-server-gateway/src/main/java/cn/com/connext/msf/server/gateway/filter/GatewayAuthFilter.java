package cn.com.connext.msf.server.gateway.filter;

import cn.com.connext.msf.framework.jwt.*;
import cn.com.connext.msf.framework.metrics.RequestTraceVariables;
import cn.com.connext.msf.framework.route.gateway.GatewayRouteRule;
import cn.com.connext.msf.framework.utils.IpUtils;
import cn.com.connext.msf.server.gateway.cache.ClientRoleCache;
import cn.com.connext.msf.server.gateway.cache.ClientTrustedHostsCache;
import cn.com.connext.msf.server.gateway.cache.ExpiredTokenCache;
import cn.com.connext.msf.server.gateway.cache.RoleAuthoritiesCache;
import cn.com.connext.msf.server.gateway.client.GatewayAuthClient;
import cn.com.connext.msf.server.gateway.constant.ErrorCode;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GatewayAuthFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(GatewayAuthFilter.class);

    private final boolean isSecure;
    private final JsonWebTokenParser jsonWebTokenParser;
    private final ClientRoleCache clientRoleCache;
    private final ClientTrustedHostsCache clientTrustedHostsCache;
    private final RoleAuthoritiesCache roleAuthoritiesCache;
    private final ExpiredTokenCache expiredTokenCache;
    private final GatewayAuthClient gatewayAuthClient;

    public GatewayAuthFilter(ClientRoleCache clientRoleCache,
                             RoleAuthoritiesCache roleAuthoritiesCache,
                             @Value("${auth.publicKey}") String publicKey,
                             @Value("${auth.secure:false}") boolean isSecure,
                             ClientTrustedHostsCache clientTrustedHostsCache,
                             ExpiredTokenCache expiredTokenCache,
                             GatewayAuthClient gatewayAuthClient) {
        this.clientRoleCache = clientRoleCache;
        this.roleAuthoritiesCache = roleAuthoritiesCache;
        this.jsonWebTokenParser = new JsonWebTokenParser(publicKey);
        this.clientTrustedHostsCache = clientTrustedHostsCache;
        this.expiredTokenCache = expiredTokenCache;
        this.gatewayAuthClient = gatewayAuthClient;
        this.isSecure = isSecure;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HashMap<String, GatewayRouteRule> allowedMethodMap = getUriMethodMap(requestContext);

        if (allowedMethodMap == null) {
            responseError(requestContext, 404);
            try {
                requestContext.getResponse().flushBuffer();
            } catch (IOException e) {}
            return null;
        }

        if (!allowedMethodMap.containsKey("*") && !allowedMethodMap.containsKey(request.getMethod())) {
            return responseError(requestContext, 400);
        }

        String jwtString = getTokenString(request);
        Claims claims = jsonWebTokenParser.getClaims(jwtString);

        String tenantId = getPayload(claims, "tenantId");
        String clientType = getPayload(claims, "clientType");
        String clientId = getPayload(claims, "sub");


        // IP校验 IP地址格式：118.25.4.253/0;115.159.125.198/0
        // 如果是客户端模式 做白名单校验 如果配置了白名单则校验是否在其中；如果没有配置或者配置为'*'不校验
        if ("client".equals(clientType)) {
            List<String> trustedHosts = clientTrustedHostsCache.getTrustedHosts(clientId);
            boolean flag = (trustedHosts == null || trustedHosts.size() == 0 || (trustedHosts.size() == 1 && "*".equals(trustedHosts.get(0))));
            if (!flag) {
                String ip = IpUtils.getIP(request);
                if (StringUtils.isBlank(ip) || !IpUtils.isInRanges(trustedHosts, ip)) {
                    logger.info("trustedHosts:" + trustedHosts + ",ip:" + ip);
                    return responseError(requestContext, 402);
                }
            }
        }

        addJwtHeader(requestContext, tenantId, clientType, clientId);


        String authority = allowedMethodMap.get(request.getMethod()).getAuthority();
        if (authority == null) {    // 公开接口方法，无需鉴权
            return null;
        }

        if (claims == null) {       // 令牌为空，拒绝进行路由
            return responseError(requestContext, 401);
        }

        if (clientId == null || clientType == null) {
            return responseError(requestContext, 401);
        }

        // 获取当前token过期时间
        long tokenExpireTime = Long.parseLong(claims.get("exp").toString()) * 1000;
        if (expiredTokenCache.expired(clientId, tokenExpireTime)) {
            return responseError(requestContext, 401);
        }

        long refreshBefore = getRefreshBefore(clientType, claims);
        if (refreshBefore > 0) {
            if (System.currentTimeMillis() > refreshBefore) {
                JwtCookieInfo jwtCookieInfo = gatewayAuthClient.refreshToken(jwtString);
                if (jwtCookieInfo != null) {
                    Cookie cookie = JsonWebTokenCookieBuilder.buildCookie(jwtCookieInfo.getDomain(), jwtCookieInfo.getJwtToken(),isSecure);
                    requestContext.getResponse().addCookie(cookie);
                }
            }
        }

        if (authority.equals("")) { // 仅需登录状态，即可访问的接口。
            return null;
        }

        List<String> roles = clientRoleCache.getClientRoles(clientType, clientId);
        for (String roleId : roles) {
            List<String> grantedAuthorities = roleAuthoritiesCache.getRoleAuthorities(roleId);
            if (grantedAuthorities.contains(authority)) {
                return null;
            }
        }

        return responseError(requestContext, 403);
    }

    private String getTokenString(HttpServletRequest request) {
        String jwtString = request.getHeader(GlobalJwtConstant.AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(jwtString)) {
            return jwtString;
        }

        return JsonWebTokenCookieParser.parse(request.getCookies());
    }


    private String getPayload(Claims claims, String name) {
        if (claims == null) {
            return null;
        }

        Object object = claims.get(name);
        return object == null ? null : object.toString();
    }

    private long getRefreshBefore(String clientType, Claims claims) {
        // 仅当客户端类型为USER时，才需要进行刷新
        if (Objects.equals(clientType, ClientTypes.USER)) {
            String refreshBeforeString = getPayload(claims, GlobalJwtConstant.JWT_REFRESH_BEFORE);
            if (refreshBeforeString != null) {
                // 转换为毫秒，避免后续频繁采用new Date()判断秒
                return Long.parseLong(refreshBeforeString) * 1000;
            }
        }
        return 0;
    }

    private void addJwtHeader(RequestContext requestContext, String tenantId, String clientType, String clientId) {
        requestContext.addZuulRequestHeader(GlobalJwtConstant.EXTERNAL_ACCESS_HEADER, "yes");

        if (clientType != null) {
            requestContext.addZuulRequestHeader(GlobalJwtConstant.JWT_HEADER_CLIENT_TYPE, clientType);
        }

        if (clientId != null) {
            requestContext.addZuulRequestHeader(GlobalJwtConstant.JWT_HEADER_CLIENT_Id, clientId);
        }

        if (tenantId != null) {
            requestContext.addZuulRequestHeader(GlobalJwtConstant.JWT_HEADER_TENANT_ID, tenantId);
        }
    }

    private HashMap<String, GatewayRouteRule> getUriMethodMap(RequestContext requestContext) {
        return (HashMap<String, GatewayRouteRule>) requestContext.get(RequestTraceVariables.URI_METHOD_MAP);
    }

    private Object responseError(RequestContext requestContext, int statusCode) {
        requestContext.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
        requestContext.setResponseStatusCode(statusCode);// 返回错误码
        requestContext.setResponseBody(ErrorCode.get(statusCode));
        return null;
    }

}
