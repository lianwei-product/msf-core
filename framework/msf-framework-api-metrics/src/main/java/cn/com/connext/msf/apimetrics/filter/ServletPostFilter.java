package cn.com.connext.msf.apimetrics.filter;

import cn.com.connext.msf.apimetrics.domain.RequestTraceManager;
import cn.com.connext.msf.apimetrics.entity.RequestTrace;
import cn.com.connext.msf.framework.metrics.RequestTraceRule;
import cn.com.connext.msf.framework.metrics.RequestTraceRuleProvider;
import cn.com.connext.msf.framework.metrics.RequestTraceTypes;
import cn.com.connext.msf.framework.metrics.RequestTraceVariables;
import cn.com.connext.msf.framework.utils.IpUtils;
import cn.com.connext.msf.framework.utils.JSON;
import org.apache.commons.io.IOUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Order(20)
@WebFilter(filterName = "ServletPostFilter", urlPatterns = "/api/*")
public class ServletPostFilter extends OncePerRequestFilter {

    private final RequestTraceManager requestTraceManager;
    private final RequestTraceRuleProvider requestTraceRuleProvider;

    public ServletPostFilter(RequestTraceManager requestTraceManager,
                             RequestTraceRuleProvider requestTraceRuleProvider) {
        this.requestTraceManager = requestTraceManager;
        this.requestTraceRuleProvider = requestTraceRuleProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        Map<String, RequestTraceRule> configs = requestTraceRuleProvider.getRequestTraceRule(httpServletRequest.getRequestURI());
        if (configs == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        RequestTraceRule requestTraceRule = configs.get(httpServletRequest.getMethod());
        if (requestTraceRule == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String traceType = requestTraceRule.getType();
        if (Objects.equals(RequestTraceTypes.NONE, traceType)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        filterChain.doFilter(requestWrapper, responseWrapper);

        long requestBeginTime = (long) httpServletRequest.getAttribute(RequestTraceVariables.REQUEST_BEGIN_TIME);
        long responseTimeTook = System.currentTimeMillis() - requestBeginTime;

        String traceId = (String) httpServletRequest.getAttribute(RequestTraceVariables.TRACE_ID);
        String spanId = (String) httpServletRequest.getAttribute(RequestTraceVariables.SPAN_ID);
        String parentSpanId = (String) httpServletRequest.getAttribute(RequestTraceVariables.PARENT_SPAN_ID);

        String tenantId = (String) httpServletRequest.getAttribute("jwt_tenantId");
        String buCode = (String) httpServletRequest.getAttribute("jwt_buCode");
        String userId = (String) httpServletRequest.getAttribute("jwt_sub");

        RequestTrace requestTrace = new RequestTrace();
        requestTrace.setTraceId(traceId);
        requestTrace.setParentSpanId(parentSpanId);
        requestTrace.setSpanId(spanId);
        requestTrace.setTenantId(tenantId);
        requestTrace.setBuCode(buCode);
        requestTrace.setUserId(userId);
        requestTrace.setRequestUri(requestWrapper.getRequestURI());
        requestTrace.setRealIp(IpUtils.getIP(httpServletRequest));
        requestTrace.setRemoteAddr(requestWrapper.getRemoteAddr());
        requestTrace.setPath(requestTraceRule.getPath());
        requestTrace.setMethod(requestWrapper.getMethod());
        requestTrace.setTraceType(traceType);
        requestTrace.setTimeTook(responseTimeTook);

        switch (traceType) {
            case RequestTraceTypes.TRACE:
                // TODO: 链路
            case RequestTraceTypes.DEBUG:
                String responseBody = IOUtils.toString(responseWrapper.getContentInputStream());
                requestTrace.setResponseBody(responseBody);
            case RequestTraceTypes.INFO:
                requestTrace.setRequestParameters(JSON.toJsonString(httpServletRequest.getParameterMap()));
                String requestBody;
                try {
                    InputStream stream = httpServletRequest.getInputStream();
                    requestBody = StreamUtils.copyToString(stream, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    requestBody = null;
                }
                requestTrace.setRequestBody(requestBody);
                break;
            default:
                break;
        }

        responseWrapper.copyBodyToResponse();
        requestTraceManager.save(requestTrace);

    }

    @Override
    public void destroy() {
    }

}
