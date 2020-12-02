package cn.com.connext.msf.server.gateway.config;

import cn.com.connext.msf.server.gateway.trace.GatewayRequestTraceRuleProvider;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GatewayRequestTraceRuleProvider.class})
@ConditionalOnClass(name = "cn.com.connext.msf.apimetrics.filter.ZuulPostFilter")
public class GatewayRequestTraceConfig {

    public GatewayRequestTraceConfig() {
        LoggerFactory.getLogger(GatewayRequestTraceConfig.class).info("Init gateway server api metrics config provider.");
    }
}
