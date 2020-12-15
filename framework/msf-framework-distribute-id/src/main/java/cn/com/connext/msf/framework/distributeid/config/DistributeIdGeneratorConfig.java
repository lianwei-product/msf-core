package cn.com.connext.msf.framework.distributeid.config;

import cn.com.connext.msf.framework.distributeid.boot.BootRunner;
import cn.com.connext.msf.framework.distributeid.domain.DistributeIdGenerator;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DistributeIdGenerator.class, BootRunner.class})
public class DistributeIdGeneratorConfig {

    public DistributeIdGeneratorConfig() {
        LoggerFactory.getLogger(DistributeIdGeneratorConfig.class).info("Init DistributeIdGenerator.");
    }

}
