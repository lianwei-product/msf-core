package cn.com.connext.msf.server.management.model;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient.EurekaServiceInstance;

import java.util.Date;

/**
 * 对象说明：服务实例信息
 * 开发人员：程瀚
 * 摘要说明：
 * 修订日期：2020-06-18 16:26:43
 */
public class ZonedServiceInstance {

    private String id;
    private String uri;
    private String zone;
    private Date onlineTime;

    private ZonedServiceInstance() {
        onlineTime = new Date();
    }

    public static ZonedServiceInstance from(String id, String zone) {
        ZonedServiceInstance zonedServiceInstance = new ZonedServiceInstance();
        zonedServiceInstance.id = id;
        zonedServiceInstance.zone = zone;
        return zonedServiceInstance;
    }

    public static ZonedServiceInstance from(ServiceInstance serviceInstance) {
        EurekaServiceInstance eurekaServiceInstance = (EurekaServiceInstance) serviceInstance;
        ZonedServiceInstance zonedServiceInstance = new ZonedServiceInstance();
        zonedServiceInstance.id = eurekaServiceInstance.getInstanceInfo().getInstanceId();
        zonedServiceInstance.uri = eurekaServiceInstance.getUri().toString();
        zonedServiceInstance.zone = eurekaServiceInstance.getInstanceInfo().getMetadata().getOrDefault("service-zone", "common");
        zonedServiceInstance.onlineTime = new Date(eurekaServiceInstance.getInstanceInfo().getLeaseInfo().getServiceUpTimestamp());
        return zonedServiceInstance;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }
}
