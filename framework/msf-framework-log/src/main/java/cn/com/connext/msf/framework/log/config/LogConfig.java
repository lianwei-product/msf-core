package cn.com.connext.msf.framework.log.config;


import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "cn.com.connext.msf.framework.log.service",
        "cn.com.connext.msf.framework.log.protector"
})
public class LogConfig {
    public LogConfig() {
        LoggerFactory.getLogger(LogConfig.class).info("Init log config...");
    }

}
