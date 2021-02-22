package cn.com.connext.msf.framework.route.gateway;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关路由表
 * 开发人员: 程瀚
 * 修订日期: 2019-06-02 21:08:51
 */
public class GatewayRoute {

    private String id;
    private String path;
    private String type;
    private String targetService;
    private String targetUrl;
    private boolean retryAble;

    private Map<String, HashMap<String, GatewayRouteRule>> routeRuleMap = Maps.newLinkedHashMap();


    public static GatewayRoute from(String id,
                                    String path,
                                    String type,
                                    String targetService,
                                    String targetUrl,
                                    boolean retryAble) {
        GatewayRoute gatewayRoute = new GatewayRoute();
        gatewayRoute.id = id;
        gatewayRoute.path = path;
        gatewayRoute.type = type;
        gatewayRoute.targetService = targetService;
        gatewayRoute.targetUrl = targetUrl;
        gatewayRoute.retryAble = retryAble;

        return gatewayRoute;
    }


    public void appendRouteRule(String uri, String httpMethod, GatewayRouteRule rule) {
        HashMap<String, GatewayRouteRule> allowedMethodMap = routeRuleMap.computeIfAbsent(uri, k -> Maps.newHashMap());
        allowedMethodMap.put(httpMethod, rule);
    }

    public HashMap<String, GatewayRouteRule> getMethodRuleMap(String uri, String method) {
        if (!GatewayPathMatcher.match(path, uri)) return null;
        HashMap<String, GatewayRouteRule> map = routeRuleMap.get(uri);
        if (map != null) {
            return map;
        }

        for (Map.Entry<String, HashMap<String, GatewayRouteRule>> entry : routeRuleMap.entrySet()) {
            if (GatewayPathMatcher.match(entry.getKey(), uri)) {
                HashMap<String, GatewayRouteRule> result = entry.getValue();
                if (result.containsKey(method)) {
                    return result;
                }
            }
        }
        return null;
    }


    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isRetryAble() {
        return retryAble;
    }

    public void setRetryAble(boolean retryAble) {
        this.retryAble = retryAble;
    }

    public Map<String, HashMap<String, GatewayRouteRule>> getRouteRuleMap() {
        return routeRuleMap;
    }
}
