package cn.com.connext.msf.apimetrics.filter;

import cn.com.connext.msf.apimetrics.provider.WebAppRequestTraceRuleBuilder;
import cn.com.connext.msf.apimetrics.provider.WebAppRequestTraceRuleProvider;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnMissingClass("com.netflix.zuul.ZuulFilter")
@Import({ServletPreFilter.class, ServletPostFilter.class,
        WebAppRequestTraceRuleProvider.class, WebAppRequestTraceRuleBuilder.class})
public class ServletFilterConfig {

    public ServletFilterConfig() {
        LoggerFactory.getLogger(ServletFilterConfig.class).info("Init api metrics servlet filter config...");
    }

}
