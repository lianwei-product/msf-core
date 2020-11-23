package cn.com.connext.msf.server.management.webapi;

import cn.com.connext.msf.framework.route.service.ServiceRoute;
import cn.com.connext.msf.server.management.constant.ManagementAuthority;
import cn.com.connext.msf.server.management.domain.RegisteredServiceManager;
import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import cn.com.connext.msf.webapp.annotation.ApiPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Api(description = "已注册服务管理接口")
@ApiPath("/api/${msf.application.name}/registered_service")
public class RegisteredServiceApi {

    private final RegisteredServiceManager registeredServiceManager;

    public RegisteredServiceApi(RegisteredServiceManager registeredServiceManager) {
        this.registeredServiceManager = registeredServiceManager;
    }

    @GetMapping
    @ApiOperation("获取服务列表")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE)
    public List<String> findServiceList() {
        return registeredServiceManager.findServiceList();
    }

    @GetMapping("/service-instance")
    @ApiOperation("获取服务实例列表")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE)
    public List<ServiceInstance> findServiceInstanceList(String serviceName) {
        return registeredServiceManager.findServiceInstance(serviceName);
    }

    @GetMapping("/service-route")
    @ApiOperation("获取服务路由")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE)
    public ServiceRoute findServiceRoute(String serviceName) {
        return registeredServiceManager.findServiceRoute(serviceName);
    }
}
