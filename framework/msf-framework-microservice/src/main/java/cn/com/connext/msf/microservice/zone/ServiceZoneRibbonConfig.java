package cn.com.connext.msf.microservice.zone;

import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientName;
import org.springframework.context.annotation.Bean;

/**
 * 服务可用区Ribbon服务实例过滤器 - 自动配置
 * 开发人员：程瀚
 * 摘要说明：当启用服务区时，将自动注入新的ServiceZoneRibbonFilter，用于替换SpringCloud默认的ServerListFilter，
 * 从而实现基于服务可用区的客户端负载匀衡。
 * 修订日期：2020-6-17 8:54:35
 */
public class ServiceZoneRibbonConfig {

    private final Logger logger = LoggerFactory.getLogger(ServiceZoneRibbonConfig.class);
    private final PropertiesFactory propertiesFactory;
    private final ServiceZoneManager serviceZoneManager;

    @RibbonClientName
    private String clientName = "client";

    public ServiceZoneRibbonConfig(PropertiesFactory propertiesFactory, ServiceZoneManager serviceZoneManager) {
        this.propertiesFactory = propertiesFactory;
        this.serviceZoneManager = serviceZoneManager;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public ServerListFilter<Server> ribbonServerListFilter(IClientConfig config) {
        logger.info("Init ServiceZoneRibbonFilter, client: {}, ConnectTimeout: {}", clientName, config.get(CommonClientConfigKey.ConnectTimeout));

        if (this.propertiesFactory.isSet(ServerListFilter.class, clientName)) {
            return this.propertiesFactory.get(ServerListFilter.class, config, clientName);
        }
        ServiceZoneRibbonFilter filter = new ServiceZoneRibbonFilter(serviceZoneManager);
        filter.initWithNiwsConfig(config);
        return filter;
    }
}
