package cn.com.connext.msf.microservice.actuator;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.microservice.zone.ServiceZone;
import cn.com.connext.msf.microservice.zone.ServiceZoneManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

/**
 * 服务可用区 - 终结点
 * 开发人员：程瀚
 * 摘要说明：该对象采用SpringBoot Actuator提供Rest接口，以便外部动态调整当前微服务实例的可用区信息。
 * 修订日期：2020-6-17 8:48:19
 */
@Endpoint(id = "service-zone")
public class ServiceZoneEndPoint {

    private final ServiceZone defaultServiceZone = ServiceZone.from("common", "*");
    private final ServiceZoneManager serviceZoneManager;

    public ServiceZoneEndPoint(@Autowired(required = false) ServiceZoneManager serviceZoneManager) {
        this.serviceZoneManager = serviceZoneManager;
    }

    @ReadOperation
    public ServiceZone current() {
        return serviceZoneManager == null ? defaultServiceZone : serviceZoneManager.current();
    }

    @WriteOperation
    public void changeZone(String zone, String availableZonesString) {
        if (serviceZoneManager == null) {
            throw new BusinessException("Service zone config not enabled, make sure msf.service-zone.enabled=true.");
        }
        serviceZoneManager.changeZone(zone, availableZonesString);
    }

}
