package cn.com.connext.msf.server.gateway.trace;

import cn.com.connext.msf.framework.metrics.RequestTraceRule;
import cn.com.connext.msf.framework.metrics.RequestTraceRuleProvider;
import cn.com.connext.msf.framework.route.gateway.GatewayRoute;
import cn.com.connext.msf.framework.route.gateway.GatewayRouteRule;
import cn.com.connext.msf.server.gateway.route.GatewayRouteManager;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class GatewayRequestTraceRuleProvider implements RequestTraceRuleProvider {

    private final GatewayRouteManager gatewayRouteManager;

    public GatewayRequestTraceRuleProvider(GatewayRouteManager gatewayRouteManager) {
        this.gatewayRouteManager = gatewayRouteManager;
    }

    @Override
    public Map<String, RequestTraceRule> getRequestTraceRule(String uri) {
        for (GatewayRoute gatewayRoute : gatewayRouteManager.getGatewayRouteMap().values()) {
            HashMap<String, GatewayRouteRule> filterMethodMap = gatewayRoute.getMethodRuleMap(uri);
            if (filterMethodMap != null) {
                HashMap<String, RequestTraceRule> configs = Maps.newHashMap();
                filterMethodMap.forEach((k, v) -> configs.putIfAbsent(k, RequestTraceRule.from(v.getPath(), v.getApiRequestTraceType())));
                return configs;
            }
        }
        return Maps.newHashMap();
    }

}
