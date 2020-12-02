package cn.com.connext.msf.apimetrics.filter;

import cn.com.connext.msf.apimetrics.domain.RequestTraceManager;
import cn.com.connext.msf.apimetrics.entity.RequestTrace;
import cn.com.connext.msf.framework.jwt.GlobalJwtConstant;
import cn.com.connext.msf.framework.metrics.RequestTraceRule;
import cn.com.connext.msf.framework.metrics.RequestTraceRuleProvider;
import cn.com.connext.msf.framework.metrics.RequestTraceTypes;
import cn.com.connext.msf.framework.metrics.RequestTraceVariables;
import cn.com.connext.msf.framework.utils.IpUtils;
import cn.com.connext.msf.framework.utils.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class ZuulPostFilter extends ZuulFilter {

    private final RequestTraceManager requestTraceManager;
    private final RequestTraceRuleProvider requestTraceRuleProvider;

    public ZuulPostFilter(RequestTraceManager requestTraceManager,
                          RequestTraceRuleProvider requestTraceRuleProvider) {
        this.requestTraceManager = requestTraceManager;
        this.requestTraceRuleProvider = requestTraceRuleProvider;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 999;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        Map<String, RequestTraceRule> configs = requestTraceRuleProvider.getRequestTraceRule(request.getRequestURI());
        if (configs != null) {
            RequestTraceRule requestTraceRule = configs.get(request.getMethod());
            if (requestTraceRule == null) {
                return null;
            }

            String traceType = requestTraceRule.getType();
            if (Objects.equals(RequestTraceTypes.NONE, traceType)) {
                return null;
            }
            long requestBeginTime = (long) requestContext.get(RequestTraceVariables.REQUEST_BEGIN_TIME);
            long responseTimeTook = System.currentTimeMillis() - requestBeginTime;

            String traceId = (String) requestContext.get(RequestTraceVariables.TRACE_ID);
            String spanId = (String) requestContext.get(RequestTraceVariables.SPAN_ID);
            String parentSpanId = (String) requestContext.get(RequestTraceVariables.PARENT_SPAN_ID);
            String tenantId = requestContext.getZuulRequestHeaders().get(GlobalJwtConstant.JWT_HEADER_TENANT_ID);
            String userId = requestContext.getZuulRequestHeaders().get(GlobalJwtConstant.JWT_HEADER_CLIENT_Id);

            RequestTrace requestTrace = new RequestTrace();
            requestTrace.setTraceId(traceId);
            requestTrace.setParentSpanId(parentSpanId);
            requestTrace.setSpanId(spanId);
            requestTrace.setTenantId(tenantId);
            requestTrace.setBuCode(null);
            requestTrace.setUserId(userId);
            requestTrace.setRequestUri(request.getRequestURI());
            requestTrace.setRealIp(IpUtils.getIP(request));
            requestTrace.setRemoteAddr(request.getRemoteAddr());
            requestTrace.setPath(requestTraceRule.getPath());
            requestTrace.setMethod(request.getMethod());
            requestTrace.setTraceType(traceType);
            requestTrace.setTimeTook(responseTimeTook);

            switch (traceType) {
                case RequestTraceTypes.TRACE:
                    // TODO: 链路
                case RequestTraceTypes.DEBUG:
                    String responseBody;
                    try {
                        responseBody = StreamUtils.copyToString(requestContext.getResponseDataStream(), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        responseBody = null;
                    }
                    requestTrace.setResponseBody(responseBody);
                    if (responseBody != null) {
                        requestContext.setResponseDataStream(new ByteArrayInputStream(responseBody.getBytes()));
                    }
                case RequestTraceTypes.INFO:
                    requestTrace.setRequestParameters(JSON.toJsonString(request.getParameterMap()));
                    String requestBody;
                    try {
                        InputStream stream = request.getInputStream();
                        requestBody = StreamUtils.copyToString(stream, StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        requestBody = null;
                    }
                    requestTrace.setRequestBody(requestBody);
                    break;
                default:

            }

            requestTraceManager.save(requestTrace);
        }

        return null;
    }

}
