package cn.com.connext.msf.framework.route.service;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 服务自动路由
 */
public class ServiceRoute {
    private String serviceName;
    private String serviceDescription;
    private List<ServiceRouteWebApi> serviceRouteWebApiList;


    private ServiceRoute() {
        serviceRouteWebApiList = Lists.newArrayList();
    }

    public static ServiceRoute from(String serviceName, String serviceDescription, List<ServiceRouteWebApi> serviceRouteWebApiList) {
        ServiceRoute serviceRoute = new ServiceRoute();
        serviceRoute.serviceName = serviceName;
        serviceRoute.serviceDescription = serviceDescription;
        serviceRoute.serviceRouteWebApiList = serviceRouteWebApiList;
        return serviceRoute;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public List<ServiceRouteWebApi> getServiceRouteWebApiList() {
        return serviceRouteWebApiList;
    }

}
