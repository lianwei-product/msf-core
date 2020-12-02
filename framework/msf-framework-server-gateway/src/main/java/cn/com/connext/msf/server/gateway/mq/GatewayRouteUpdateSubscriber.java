package cn.com.connext.msf.server.gateway.mq;


import cn.com.connext.msf.server.gateway.filter.GatewayRouteFilter;
import cn.com.connext.msf.server.gateway.route.GatewayRouteLocator;
import cn.com.connext.msf.server.gateway.route.GatewayRouteManager;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Payload;

@EnableBinding({GatewayRouteUpdatePipeline.class})
public class GatewayRouteUpdateSubscriber {

    private final GatewayRouteManager gatewayRouteManager;
    private final GatewayRouteFilter gatewayRouteFilter;
    private final GatewayRouteLocator gatewayRouteLocator;

    private final ApplicationEventPublisher applicationEventPublisher;


    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiringInspection"})
    public GatewayRouteUpdateSubscriber(GatewayRouteManager gatewayRouteManager,
                                        GatewayRouteFilter gatewayRouteFilter,
                                        GatewayRouteLocator gatewayRouteLocator,
                                        ApplicationEventPublisher applicationEventPublisher) {
        this.gatewayRouteManager = gatewayRouteManager;
        this.gatewayRouteFilter = gatewayRouteFilter;
        this.gatewayRouteLocator = gatewayRouteLocator;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @StreamListener(GatewayRouteUpdatePipeline.RECV)
    public void receiver(@Payload String routeId) {
        gatewayRouteManager.updateRoutes(routeId);
        gatewayRouteFilter.updateRoutes(gatewayRouteManager.getGatewayRouteMap());
        gatewayRouteLocator.updateRoutes(gatewayRouteManager.getGatewayRouteMap());

        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(gatewayRouteLocator);
        applicationEventPublisher.publishEvent(routesRefreshedEvent);
    }
}
