package cn.com.connext.msf.server.management.domain;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.route.gateway.GatewayRouteTypes;
import cn.com.connext.msf.framework.route.service.ServiceRoute;
import cn.com.connext.msf.framework.route.service.ServiceRouteWebApi;
import cn.com.connext.msf.framework.route.service.ServiceRouteWebMethod;
import cn.com.connext.msf.server.management.entity.Route;
import cn.com.connext.msf.server.management.entity.RouteApi;
import cn.com.connext.msf.server.management.entity.RouteApiMethod;
import cn.com.connext.msf.server.management.mq.GatewayRouteUpdatePublisher;
import cn.com.connext.msf.server.management.repository.RouteApiMethodRepository;
import cn.com.connext.msf.server.management.repository.RouteApiRepository;
import cn.com.connext.msf.server.management.repository.RouteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RouteManager {

    private final RegisteredServiceManager registeredServiceManager;
    private final RouteRepository routeRepository;
    private final RouteApiRepository routeApiRepository;
    private final RouteApiMethodRepository routeApiMethodRepository;
    private final GatewayRouteUpdatePublisher gatewayRouteUpdatePublisher;

    public RouteManager(RegisteredServiceManager registeredServiceManager,
                        RouteRepository routeRepository,
                        RouteApiRepository routeApiRepository,
                        RouteApiMethodRepository routeApiMethodRepository,
                        GatewayRouteUpdatePublisher gatewayRouteUpdatePublisher) {
        this.registeredServiceManager = registeredServiceManager;
        this.routeRepository = routeRepository;
        this.routeApiRepository = routeApiRepository;
        this.routeApiMethodRepository = routeApiMethodRepository;
        this.gatewayRouteUpdatePublisher = gatewayRouteUpdatePublisher;
    }

    public Route registerFromService(String serviceName, boolean overwrite) {
        Route existRoute = findExistGatewayRoute(serviceName);
        Map<String, RouteApiMethod> routeApiMethodMap = null;
        if (existRoute != null) {
            if (!overwrite) {
                throw new BusinessException("由微服务注册网关路由失败，错误原因：服务已经注册。");
            }

            QueryInfo queryInfo = QueryInfo.from("routeId eq {0}", existRoute.getId());
            routeApiMethodMap = routeApiMethodRepository.findList(queryInfo).stream().collect(Collectors.toMap(RouteApiMethod::getUrlHttpMethod, routeApiMethod -> routeApiMethod));
            routeApiMethodRepository.delete(queryInfo);
            routeApiRepository.delete(queryInfo);
            routeRepository.delete(existRoute.getId());
        }

        try {
            ServiceRoute serviceRoute = registeredServiceManager.findServiceRoute(serviceName);
            return registerFromServiceRoute(serviceRoute, existRoute, routeApiMethodMap);
        } catch (Exception e) {
            throw new BusinessException("由微服务注册网关路由失败，错误原因：" + e.getMessage());
        }
    }

    public List<Route> getRegisteredRoute() {
        return routeRepository.findList();
    }

    public Route registerFromServiceRoute(ServiceRoute serviceRoute, Route existRoute, Map<String, RouteApiMethod> routeApiMethodMap) {
        Route route = convertGatewayRoute(serviceRoute);
        if (existRoute != null) {
            route.setId(existRoute.getId());
        }

        List<ServiceRouteWebApi> serviceRouteWebApiList = serviceRoute.getServiceRouteWebApiList();
        serviceRouteWebApiList.forEach(serviceRouteWebApi -> {
            RouteApi routeApi = convertFromServiceRouteWebApi(route, serviceRouteWebApi);
            routeApiRepository.create(routeApi);

            serviceRouteWebApi.getWebMethods().forEach(serviceRouteWebMethod -> {
                RouteApiMethod routeApiMethod = convertFromServiceRouteWebMethod(route,
                        routeApi,
                        serviceRouteWebMethod,
                        routeApiMethodMap);
                routeApiMethodRepository.create(routeApiMethod);
            });
        });

        routeRepository.create(route);
        gatewayRouteUpdatePublisher.send(route.getId());
        return route;
    }

    public boolean isNeedInit() {
        return routeRepository.count() == 0;
    }

    private Route findExistGatewayRoute(String serviceName) {
        String path = "/api/" + serviceName + "/**";
        QueryInfo queryInfo = QueryInfo.from("targetService eq {0} AND path eq {1}", serviceName, path);
        return routeRepository.findFirst(queryInfo);
    }

    private Route convertGatewayRoute(ServiceRoute serviceRoute) {
        Route route = new Route();
        route.setName(serviceRoute.getServiceDescription());
        route.setPath("/api/" + serviceRoute.getServiceName() + "/**");
        route.setType(GatewayRouteTypes.Service);
        route.setTargetService(serviceRoute.getServiceName());
        return route;
    }

    private RouteApi convertFromServiceRouteWebApi(Route route,
                                                   ServiceRouteWebApi serviceRouteWebApi) {
        RouteApi routeApi = new RouteApi();
        routeApi.setRouteId(route.getId());
        routeApi.setName(serviceRouteWebApi.getName());
        routeApi.setDescription(serviceRouteWebApi.getDescription());
        return routeApi;
    }

    private RouteApiMethod convertFromServiceRouteWebMethod(Route route,
                                                            RouteApi routeApi,
                                                            ServiceRouteWebMethod serviceRouteWebMethod,
                                                            Map<String, RouteApiMethod> routeApiMethodMap) {
        RouteApiMethod routeApiMethod = new RouteApiMethod();
        routeApiMethod.setRouteId(route.getId());
        routeApiMethod.setRouteApiId(routeApi.getId());
        routeApiMethod.setName(serviceRouteWebMethod.getName());
        routeApiMethod.setDescription(serviceRouteWebMethod.getDescription());
        routeApiMethod.setHttpMethod(serviceRouteWebMethod.getHttpMethod());
        routeApiMethod.setUri(serviceRouteWebMethod.getUri());
        routeApiMethod.setNoAuth(serviceRouteWebMethod.isNoAuth());
        routeApiMethod.setAuthority(serviceRouteWebMethod.getAuthority());
        if (routeApiMethodMap != null && routeApiMethodMap.size() > 0 && routeApiMethodMap.containsKey(routeApiMethod.getUrlHttpMethod())) {
            routeApiMethod.setConfig(routeApiMethodMap.get(routeApiMethod.getUrlHttpMethod()).getConfig());
        }
        return routeApiMethod;
    }
}
