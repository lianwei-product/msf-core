package cn.com.connext.msf.apimetrics.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "cn.com.connext.msf.apimetrics.domain",
        "cn.com.connext.msf.apimetrics.entity"
})
public class ApiMetricsConfig {

    public ApiMetricsConfig() {
        LoggerFactory.getLogger(ApiMetricsConfig.class).info("Starting micro-service API metrics server...");
    }

}
