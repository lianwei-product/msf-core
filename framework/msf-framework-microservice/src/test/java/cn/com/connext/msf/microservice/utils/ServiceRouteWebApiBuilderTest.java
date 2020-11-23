package cn.com.connext.msf.microservice.utils;


import cn.com.connext.msf.framework.route.service.ServiceRouteWebApi;
import cn.com.connext.msf.framework.utils.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.List;

public class ServiceRouteWebApiBuilderTest {

    @Test
    @SuppressWarnings("ConstantConditions")
    public void buildTest() {
        ServiceRouteWebApiBuilder serviceRouteWebApiBuilder = new ServiceRouteWebApiBuilder();

        RequestMappingInfo mappingInfo01 = new RequestMappingInfo("", new PatternsRequestCondition("/api/users"), new RequestMethodsRequestCondition(RequestMethod.GET), null, null, null, null, null);
        HandlerMethod method01 = new HandlerMethod(UserApi.getInstance(), UserApi.getMethod());
        serviceRouteWebApiBuilder.append(mappingInfo01, method01);

        RequestMappingInfo mappingInfo02 = new RequestMappingInfo("", new PatternsRequestCondition("/api/roles"), new RequestMethodsRequestCondition(RequestMethod.GET), null, null, null, null, null);
        HandlerMethod method02 = new HandlerMethod(RoleApi.getInstance(), RoleApi.getMethod());
        serviceRouteWebApiBuilder.append(mappingInfo02, method02);

        RequestMappingInfo mappingInfo03 = new RequestMappingInfo("", new PatternsRequestCondition("/api/roles"), new RequestMethodsRequestCondition(RequestMethod.POST), null, null, null, null, null);
        HandlerMethod method03 = new HandlerMethod(RoleApi.getInstance(), RoleApi.getMethod());
        serviceRouteWebApiBuilder.append(mappingInfo03, method03);


        List<ServiceRouteWebApi> result = serviceRouteWebApiBuilder.build();
        System.out.println(JSON.toIndentJsonString(result));

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(1, result.get(0).getWebMethods().size());
        Assert.assertEquals(2, result.get(1).getWebMethods().size());
    }

}