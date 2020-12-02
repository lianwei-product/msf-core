package cn.com.connext.msf.server.gateway.route;

import cn.com.connext.msf.framework.route.gateway.GatewayRoute;
import cn.com.connext.msf.framework.route.gateway.GatewayRouteTypes;
import cn.com.connext.msf.framework.metrics.RequestTraceVariables;
import com.google.common.collect.Maps;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GatewayRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    private final static Logger logger = LoggerFactory.getLogger(GatewayRouteLocator.class);
    private final ReentrantReadWriteLock lock;
    private boolean needRefresh;
    private LinkedHashMap<String, ZuulRoute> zuulRouteLinkedHashMap = Maps.newLinkedHashMap();


    public GatewayRouteLocator(ServerProperties serverProperties,
                               ZuulProperties zuulProperties) {
        super(serverProperties.getServlet().getContextPath(), zuulProperties);
        this.lock = new ReentrantReadWriteLock();
        this.needRefresh = false;
    }

    public void updateRoutes(Map<String, GatewayRoute> gatewayRouteMap) {
        lock.writeLock().lock();
        zuulRouteLinkedHashMap = Maps.newLinkedHashMap();
        gatewayRouteMap.values().forEach(gatewayRouteTable -> {
            ZuulRoute zuulRoute = convert(gatewayRouteTable);
            zuulRouteLinkedHashMap.put(zuulRoute.getPath(), zuulRoute);
        });

        this.needRefresh = true;
        lock.writeLock().unlock();
    }


    private ZuulRoute convert(GatewayRoute gatewayRoute) {
        ZuulRoute zuulRoute = new ZuulRoute();
        zuulRoute.setId(gatewayRoute.getId());
        zuulRoute.setPath(gatewayRoute.getPath());
        if (Objects.equals(gatewayRoute.getType(), GatewayRouteTypes.Service)) {
            zuulRoute.setStripPrefix(false);
            zuulRoute.setServiceId(gatewayRoute.getTargetService());
        } else {
            zuulRoute.setStripPrefix(true);
            zuulRoute.setUrl(gatewayRoute.getTargetUrl());
        }
        zuulRoute.setRetryable(gatewayRoute.isRetryAble());
        return zuulRoute;
    }


    @Override
    public List<Route> getRoutes() {
        lock.readLock().lock();
        try {
            return super.getRoutes();
        } finally {
            lock.readLock().unlock();
        }
    }


    @Override
    protected LinkedHashMap<String, ZuulRoute> locateRoutes() {
        lock.writeLock().lock();
        LinkedHashMap<String, ZuulRoute> routeMap = Maps.newLinkedHashMap();
        try {
            routeMap.putAll(super.locateRoutes());
            routeMap.putAll(zuulRouteLinkedHashMap);
            logger.info("Update gateway route table, route count: {}", routeMap.size());
            return routeMap;
        } finally {
            needRefresh = false;
            lock.writeLock().unlock();
        }
    }

    @Override
    public void refresh() {
        if (needRefresh) {
            super.doRefresh();
        }
    }

    @Override
    public Route getMatchingRoute(final String path) {
        RequestContext requestContext = RequestContext.getCurrentContext();
        return (Route) requestContext.get(RequestTraceVariables.CURRENT_ROUTE);
    }

    public Route preGetMatchingRoute(final String path) {
        return getSimpleMatchingRoute(path);
    }

}
