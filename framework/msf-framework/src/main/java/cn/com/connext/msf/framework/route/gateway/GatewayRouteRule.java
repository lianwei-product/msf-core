package cn.com.connext.msf.framework.route.gateway;


public class GatewayRouteRule {

    private String path;
    private String authority;
    private String apiRequestTraceType;


    public static GatewayRouteRule from(String path, String authority, String apiRequestTraceType) {
        GatewayRouteRule gatewayRouteRule = new GatewayRouteRule();
        gatewayRouteRule.path = path;
        gatewayRouteRule.authority = authority;
        gatewayRouteRule.apiRequestTraceType = apiRequestTraceType;
        return gatewayRouteRule;
    }

    public String getPath() {
        return path;
    }

    public String getAuthority() {
        return authority;
    }

    public String getApiRequestTraceType() {
        return apiRequestTraceType;
    }


}
