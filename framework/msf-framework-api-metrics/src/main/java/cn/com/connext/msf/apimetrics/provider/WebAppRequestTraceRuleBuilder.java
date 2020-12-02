package cn.com.connext.msf.apimetrics.provider;

import cn.com.connext.msf.framework.metrics.RequestTrace;
import cn.com.connext.msf.framework.metrics.RequestTraceRule;
import com.google.common.collect.Maps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.util.HashMap;
import java.util.Map;

@ConditionalOnBean(WebAppRequestTraceRuleProvider.class)
public class WebAppRequestTraceRuleBuilder {

    private final RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping;

    private Map<String, Map<String, RequestTraceRule>> requestTraceRuleMap;

    public WebAppRequestTraceRuleBuilder(RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping) {
        this.requestMappingInfoHandlerMapping = requestMappingInfoHandlerMapping;
        this.requestTraceRuleMap = buildRequestTraceRule();
    }

    // TODO: add more fields to support other metrics
    private Map<String, Map<String, RequestTraceRule>> buildRequestTraceRule() {
        Map<String, Map<String, RequestTraceRule>> apiMetricsConfigsMap = Maps.newHashMap();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingInfoHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
            HandlerMethod handlerMethod = requestMappingInfoHandlerMethodEntry.getValue();
            RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();
            RequestTrace requestTrace = handlerMethod.getMethodAnnotation(RequestTrace.class);
            if (requestTrace != null) {
                PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                String url = patternsCondition.getPatterns().iterator().next();
                String httpMethod = methodCondition.getMethods().iterator().next().name();
                String type = requestTrace.type();
                HashMap<String, RequestTraceRule> hashMap = new HashMap<>();
                hashMap.putIfAbsent(httpMethod, RequestTraceRule.from(url, type));
                apiMetricsConfigsMap.putIfAbsent(url, hashMap);
            }
        }

        return apiMetricsConfigsMap;
    }

    public Map<String, Map<String, RequestTraceRule>> getRequestTraceConfigs() {
        return requestTraceRuleMap;
    }


}
