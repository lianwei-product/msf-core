package cn.com.connext.msf.framework.aviator;

import cn.com.connext.msf.framework.aviator.functions.AviatorTransfer;
import cn.com.connext.msf.framework.aviator.functions.AviatorUUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = "msf.aviator.enable", havingValue = "true", matchIfMissing = true)
@Import({AviatorUUIDUtil.class, AviatorTransfer.class})
public class AviatorManagerConfig {

    @Bean
    @SuppressWarnings({"SpringJavaAutowiringInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    public AviatorManager aviatorManager(@Autowired(required = false) List<AviatorImportFunction> functionList) {
        return new AviatorManager(functionList);
    }
}
