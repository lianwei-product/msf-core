package cn.com.connext.msf.framework.server.notify.config;

import cn.com.connext.msf.framework.server.notify.domain.NotifyManager;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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

}
