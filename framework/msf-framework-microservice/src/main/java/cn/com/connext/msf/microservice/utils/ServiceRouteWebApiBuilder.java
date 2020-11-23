package cn.com.connext.msf.microservice.utils;

import cn.com.connext.msf.framework.route.service.ServiceRouteWebApi;
import cn.com.connext.msf.framework.route.service.ServiceRouteWebMethod;
import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.List;
import java.util.Map;

public class ServiceRouteWebApiBuilder {

    private final Map<String, ServiceRouteWebApi> serviceRouteWebApiMap = Maps.newHashMap();

    public void append(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        ApiAuthority apiAuthority = handlerMethod.getMethodAnnotation(ApiAuthority.class);
        if (apiAuthority == null) {
            return;
        }

        RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();
        if (methodCondition.getMethods().isEmpty()) {
            return;
        }

        Class clazz = handlerMethod.getMethod().getDeclaringClass();
        ServiceRouteWebApi serviceRouteWebApi = serviceRouteWebApiMap.get(clazz.getName());
        if (serviceRouteWebApi == null) {
            serviceRouteWebApi = ServiceRouteWebApi.from(clazz.getSimpleName(), clazz.getName(), getWebApiDescription(clazz));
            serviceRouteWebApiMap.put(clazz.getName(), serviceRouteWebApi);
        }

        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
        String url = patternsCondition.getPatterns().iterator().next();
        String httpMethod = methodCondition.getMethods().iterator().next().name();
        String name = handlerMethod.getMethod().getName();
        boolean noAuth = apiAuthority.noAuth();
        String authority = apiAuthority.value();
        String description = getWebMethodDescription(handlerMethod);
        serviceRouteWebApi.appendMethod(ServiceRouteWebMethod.from(url, httpMethod, name, noAuth, authority, description));
    }

    public List<ServiceRouteWebApi> build() {
        return Lists.newArrayList(serviceRouteWebApiMap.values());
    }

    private String getWebApiDescription(Class clazz) {
        Api api = (Api) clazz.getAnnotation(Api.class);
        String description = null;
        if (api != null) {
            //noinspection deprecation
            if (!StringUtils.isEmpty(api.description())) {
                //noinspection deprecation
                description = api.description();
            }

            if (api.tags().length > 0 && !StringUtils.isEmpty(api.tags()[0])) {
                description = api.tags()[0];
            }
        }
        return description == null ? clazz.getSimpleName() : description;
    }

    private String getWebMethodDescription(HandlerMethod handlerMethod) {
        ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
        return apiOperation == null ? handlerMethod.getMethod().getName() : apiOperation.value();
    }
}
