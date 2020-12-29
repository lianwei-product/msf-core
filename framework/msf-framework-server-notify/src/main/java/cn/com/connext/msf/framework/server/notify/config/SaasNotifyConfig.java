package cn.com.connext.msf.framework.server.notify.config;

import cn.com.connext.msf.framework.server.notify.domain.LocalNotifyManager;
import cn.com.connext.msf.framework.server.notify.domain.NotifyManager;
import cn.com.connext.msf.framework.server.notify.processor.NotificationSendEventProcessor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan({
        "cn.com.connext.msf.framework.server.notify.processor",
        "cn.com.connext.msf.framework.server.notify.openapi",
        "cn.com.connext.msf.framework.server.notify.domain",
        "cn.com.connext.msf.framework.server.notify.mq"
})
public class SaasNotifyConfig {

    public SaasNotifyConfig() {
        LoggerFactory.getLogger(SaasNotifyConfig.class).info("Init uaa changed mq model");
    }

    @Bean
    @ConditionalOnMissingBean(NotifyManager.class)
    public NotifyManager notifyManager(List<NotificationSendEventProcessor> processChain) {
        return new LocalNotifyManager(processChain);
    }

}
