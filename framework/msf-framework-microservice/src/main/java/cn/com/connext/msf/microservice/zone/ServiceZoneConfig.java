package cn.com.connext.msf.microservice.zone;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 服务可用区 - 自动配置
 * 开发人员：程瀚
 * 摘要说明：通过将同一微服务的不同实例，在逻辑上划分到不同的服务可用区，从而实现微服务的流量隔离。通过该方案，可以使租户、品牌享有
 * 专属的微服务实例成为可能，而微服务在物理部署时无需加以区别，可以通过MMC进行动态调整。
 * 修订日期：2020-6-17 8:45:33
 */
@Configuration
@ConditionalOnProperty(name = "msf.service-zone.enabled", havingValue = "true")
@RibbonClients(defaultConfiguration = ServiceZoneRibbonConfig.class)
@Import({ServiceZoneManager.class})
public class ServiceZoneConfig {

    public ServiceZoneConfig() {
        LoggerFactory.getLogger(ServiceZoneConfig.class).info("Starting service zone configuration");
    }

}
