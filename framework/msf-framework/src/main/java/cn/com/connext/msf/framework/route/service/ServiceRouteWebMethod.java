package cn.com.connext.msf.framework.route.service;

public class ServiceRouteWebMethod {

    private String uri;
    private String httpMethod;
    private String name;
    private boolean noAuth;
    private String authority;
    private String description;

    private ServiceRouteWebMethod() {
    }

    public static ServiceRouteWebMethod from(String uri, String httpMethod, String name, boolean noAuth, String authority, String description) {
        ServiceRouteWebMethod serviceRouteWebMethod = new ServiceRouteWebMethod();
        serviceRouteWebMethod.uri = uri;
        serviceRouteWebMethod.httpMethod = httpMethod;
        serviceRouteWebMethod.name = name;
        serviceRouteWebMethod.noAuth = noAuth;
        serviceRouteWebMethod.authority = authority;
        serviceRouteWebMethod.description = description;
        return serviceRouteWebMethod;
    }


    public String getUri() {
        return uri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getName() {
        return name;
    }

    public boolean isNoAuth() {
        return noAuth;
    }

    public String getAuthority() {
        return authority;
    }

    public String getDescription() {
        return description;
    }
}
