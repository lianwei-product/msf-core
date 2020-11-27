package cn.com.connext.msf.framework.saas.notify.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "cn.com.connext.msf.framework.saas.notify.consumer",
        "cn.com.connext.msf.framework.saas.notify.publisher",
        "cn.com.connext.msf.framework.saas.notify.processor",
        "cn.com.connext.msf.framework.saas.notify.openapi"
})
public class SaasNotifyConfig {

    public SaasNotifyConfig() {
        LoggerFactory.getLogger(SaasNotifyConfig.class).info("Init uaa changed mq notify");
    }

}
