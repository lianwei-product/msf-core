package cn.com.connext.msf.framework.route.service;

import com.google.common.collect.Lists;

import java.util.List;

public class ServiceRouteWebApi {
    private String name;
    private String fullName;
    private String description;
    private List<ServiceRouteWebMethod> webMethods;

    private ServiceRouteWebApi() {
        webMethods = Lists.newArrayList();
    }

    public static ServiceRouteWebApi from(String name, String fullName, String description) {
        ServiceRouteWebApi serviceRouteWebApi = new ServiceRouteWebApi();
        serviceRouteWebApi.name = name;
        serviceRouteWebApi.fullName = fullName;
        serviceRouteWebApi.description = description;
        return serviceRouteWebApi;
    }

    public void appendMethod(ServiceRouteWebMethod serviceRouteWebMethod) {
        webMethods.add(serviceRouteWebMethod);
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public List<ServiceRouteWebMethod> getWebMethods() {
        return webMethods;
    }
}
