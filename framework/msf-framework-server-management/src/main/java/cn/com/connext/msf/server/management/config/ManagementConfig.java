package cn.com.connext.msf.server.management.config;

import cn.com.connext.msf.server.management.processor.BaseManagedSendNotificationProcessor;
import cn.com.connext.msf.server.management.processor.ManagedSendNotificationProcessor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "cn.com.connext.msf.server.management.boot",
        "cn.com.connext.msf.server.management.domain",
        "cn.com.connext.msf.server.management.entity",
        "cn.com.connext.msf.server.management.webapi",
        "cn.com.connext.msf.server.management.service",
        "cn.com.connext.msf.server.management.repository",
        "cn.com.connext.msf.server.management.validator",
        "cn.com.connext.msf.server.management.mq",
        "cn.com.connext.msf.server.management.processor"
})
public class ManagementConfig {

    public ManagementConfig() {
        LoggerFactory.getLogger(ManagementConfig.class).info("Starting micro-service management server...");
    }

    @Bean
    @ConditionalOnMissingBean(ManagedSendNotificationProcessor.class)
    public ManagedSendNotificationProcessor authoritiesValidatorProvider() {
        return new BaseManagedSendNotificationProcessor();
    }
}
