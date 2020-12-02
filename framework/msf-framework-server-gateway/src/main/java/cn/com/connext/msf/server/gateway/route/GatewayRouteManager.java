package cn.com.connext.msf.server.gateway.route;

import cn.com.connext.msf.framework.route.gateway.GatewayRoute;
import cn.com.connext.msf.server.gateway.client.GatewayRouteClient;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GatewayRouteManager {

    private final static Logger logger = LoggerFactory.getLogger(GatewayRouteManager.class);
    private final GatewayRouteClient gatewayRouteClient;

    private Map<String, GatewayRoute> gatewayRouteMap;

    public GatewayRouteManager(GatewayRouteClient gatewayRouteClient) {
        this.gatewayRouteClient = gatewayRouteClient;
        this.gatewayRouteMap = Maps.newConcurrentMap();
        gatewayRouteClient.findList().forEach(gatewayRouteTable -> {
            gatewayRouteMap.put(gatewayRouteTable.getId(), gatewayRouteTable);
        });
        logger.info("Locate gateway route table, route count:" + gatewayRouteMap.size());
    }

    public Map<String, GatewayRoute> getGatewayRouteMap() {
        return gatewayRouteMap;
    }

    public void updateRoutes(String routeId) {
        logger.info("Update gateway route table, route id:" + routeId);
        GatewayRoute gatewayRoute = gatewayRouteClient.findItem(routeId);
        if (gatewayRoute == null) {
            gatewayRouteMap.remove(routeId);
        } else {
            gatewayRouteMap.put(routeId, gatewayRoute);
        }
    }
}
