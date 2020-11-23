package cn.com.connext.msf.server.management.domain;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.route.gateway.GatewayRoute;
import cn.com.connext.msf.framework.route.gateway.GatewayRouteRule;
import cn.com.connext.msf.server.management.entity.Route;
import cn.com.connext.msf.server.management.entity.RouteApiMethod;
import cn.com.connext.msf.server.management.service.RouteApiMethodService;
import cn.com.connext.msf.server.management.service.RouteService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatewayRouteManager {
    private final RouteService routeService;
    private final RouteApiMethodService routeApiMethodService;

    public GatewayRouteManager(RouteService routeService,
                               RouteApiMethodService routeApiMethodService) {
        this.routeService = routeService;
        this.routeApiMethodService = routeApiMethodService;
    }

    public GatewayRoute findItem(String id) {
        Route route = routeService.findItem(id);
        return convertFromGatewayRoute(route);
    }

    public List<GatewayRoute> findList() {
        List<GatewayRoute> gatewayRouteList = Lists.newArrayList();
        List<Route> routeList = routeService.findList(null);
        routeList.forEach(gatewayRoute -> {
            GatewayRoute gatewayRouteTable = convertFromGatewayRoute(gatewayRoute);
            if (gatewayRouteTable != null) {
                gatewayRouteList.add(gatewayRouteTable);
            }
        });
        return gatewayRouteList;
    }

    private GatewayRoute convertFromGatewayRoute(Route route) {
        if (route == null || !route.isEnable()) {
            return null;
        }

        GatewayRoute gatewayRoute = GatewayRoute.from(route.getId(), route.getPath(), route.getType(), route.getTargetService(),
                route.getTargetUrl(), route.isRetryAble());

        QueryInfo queryInfo = QueryInfo.from("routeId eq {0}", route.getId());
        List<RouteApiMethod> apiMethods = routeApiMethodService.findList(queryInfo);
        apiMethods.sort((o1, o2) -> o2.getUri().length() - o1.getUri().length());
        apiMethods.forEach(method -> {
            String authority = method.isNoAuth() ? null : method.getAuthority();
            String apiRequestTraceType = method.getConfig().getLogLevel();
            GatewayRouteRule config = GatewayRouteRule.from(method.getUri(), authority, apiRequestTraceType);
            gatewayRoute.appendRouteRule(method.getUri(), method.getHttpMethod(), config);
        });

        return gatewayRoute;
    }

}
