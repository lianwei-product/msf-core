package cn.com.connext.msf.server.gateway.config;

import cn.com.connext.msf.server.gateway.client.GatewayRouteClient;
import cn.com.connext.msf.server.gateway.filter.GatewayRouteFilter;
import cn.com.connext.msf.server.gateway.mq.GatewayRouteUpdateSubscriber;
import cn.com.connext.msf.server.gateway.route.GatewayRouteLocator;
import cn.com.connext.msf.server.gateway.route.GatewayRouteManager;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnExpression("${msf.management.enabled:true}")
@EnableFeignClients(basePackageClasses = {GatewayRouteClient.class})
@Import({GatewayRouteFilter.class,
        GatewayRouteLocator.class,
        GatewayRouteManager.class,
        GatewayRouteUpdateSubscriber.class})
public class GatewayRouteConfig {

    public GatewayRouteConfig(GatewayRouteFilter gatewayRouteFilter,
                              GatewayRouteLocator gatewayRouteLocator,
                              GatewayRouteManager gatewayRouteManager) {
        gatewayRouteFilter.updateRoutes(gatewayRouteManager.getGatewayRouteMap());
        gatewayRouteLocator.updateRoutes(gatewayRouteManager.getGatewayRouteMap());
        LoggerFactory.getLogger(GatewayRouteConfig.class).info("Init gateway server dynamic route module.");
    }
}
