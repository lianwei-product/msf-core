package cn.com.connext.msf.apimetrics.provider;

import cn.com.connext.msf.framework.metrics.RequestPathMatcher;
import cn.com.connext.msf.framework.metrics.RequestTraceRule;
import cn.com.connext.msf.framework.metrics.RequestTraceRuleProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.Map;

@ConditionalOnMissingBean(RequestTraceRuleProvider.class)
public class WebAppRequestTraceRuleProvider implements RequestTraceRuleProvider {

    private final WebAppRequestTraceRuleBuilder webAppRequestTraceRuleBuilder;

    public WebAppRequestTraceRuleProvider(WebAppRequestTraceRuleBuilder webAppRequestTraceRuleBuilder) {
        this.webAppRequestTraceRuleBuilder = webAppRequestTraceRuleBuilder;
    }

    @Override
    public Map<String, RequestTraceRule> getRequestTraceRule(String uri) {
        Map<String, Map<String, RequestTraceRule>> requestTraceConfigMap = webAppRequestTraceRuleBuilder.getRequestTraceConfigs();
        if (requestTraceConfigMap != null) {
            for (String urlPattern : requestTraceConfigMap.keySet()) {
                if (RequestPathMatcher.match(urlPattern, uri)) {
                    return requestTraceConfigMap.get(urlPattern);
                }
            }
        }
        return null;
    }

}
