package cn.com.connext.msf.microservice.actuator;

import cn.com.connext.msf.framework.route.service.ServiceRoute;
import cn.com.connext.msf.framework.route.service.ServiceRouteWebApi;
import cn.com.connext.msf.microservice.utils.ServiceRouteWebApiBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.util.List;
import java.util.Map;

@Endpoint(id = "service-route")
public class ServiceRouteEndPoint {
    private final RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping;
    private final String serviceName;
    private final String serviceDescription;

    public ServiceRouteEndPoint(RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping,
                                @Value("${msf.application.name}") String serviceName,
                                @Value("${msf.application.description}") String serviceDescription) {
        this.requestMappingInfoHandlerMapping = requestMappingInfoHandlerMapping;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
    }


    @ReadOperation
    public ServiceRoute getRoute() {
        ServiceRouteWebApiBuilder serviceRouteWebApiBuilder = new ServiceRouteWebApiBuilder();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingInfoHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
            HandlerMethod handlerMethod = requestMappingInfoHandlerMethodEntry.getValue();
            serviceRouteWebApiBuilder.append(requestMappingInfo, handlerMethod);
        }

        List<ServiceRouteWebApi> serviceRouteWebApiList = serviceRouteWebApiBuilder.build();
        return ServiceRoute.from(serviceName, serviceDescription, serviceRouteWebApiList);
    }
}
