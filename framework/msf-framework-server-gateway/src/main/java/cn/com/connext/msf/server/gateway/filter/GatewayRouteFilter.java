package cn.com.connext.msf.server.gateway.filter;

import cn.com.connext.msf.framework.metrics.RequestTraceVariables;
import cn.com.connext.msf.framework.route.gateway.GatewayRoute;
import cn.com.connext.msf.framework.route.gateway.GatewayRouteRule;
import cn.com.connext.msf.server.gateway.route.GatewayRouteLocator;
import com.google.common.collect.Maps;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.Route;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GatewayRouteFilter extends ZuulFilter {

    private final GatewayRouteLocator routeLocator;
    private final ReentrantReadWriteLock lock;

    private Map<String, GatewayRoute> gatewayRouteMap;

    public GatewayRouteFilter(GatewayRouteLocator routeLocator) {
        this.routeLocator = routeLocator;
        this.lock = new ReentrantReadWriteLock();
        this.gatewayRouteMap = Maps.newHashMap();
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        RequestContext requestContext = RequestContext.getCurrentContext();

        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();

        Route route = routeLocator.preGetMatchingRoute(uri);
        requestContext.set(RequestTraceVariables.CURRENT_ROUTE, route);

        HashMap<String, GatewayRouteRule> uriMethodMap = getUriMethodMap(route, uri);
        requestContext.set(RequestTraceVariables.URI_METHOD_MAP, uriMethodMap);

        return null;
    }

    public void updateRoutes(Map<String, GatewayRoute> gatewayRouteMap) {
        lock.writeLock().lock();
        this.gatewayRouteMap = gatewayRouteMap;
        lock.writeLock().unlock();
    }

    private HashMap<String, GatewayRouteRule> getUriMethodMap(Route route, String uri) {
        lock.readLock().lock();
        GatewayRoute gatewayRoute = gatewayRouteMap.get(route.getId());

        try {
            if (gatewayRoute != null) {
                HashMap<String, GatewayRouteRule> allowedMethodMap = gatewayRoute.getMethodRuleMap(uri);
                if (allowedMethodMap != null) {
                    return allowedMethodMap;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }


}