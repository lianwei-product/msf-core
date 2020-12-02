package cn.com.connext.msf.framework.management;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.util.Collections;
import java.util.List;

/**
 * 已注册服务管理器
 */
public class BaseServiceManager {

    private final EurekaDiscoveryClient discoveryClient;

    public BaseServiceManager(EurekaDiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List<String> findServiceList() {
        List<String> serviceList = discoveryClient.getServices();
        Collections.sort(serviceList);
        return serviceList;
    }

    public List<ServiceInstance> findServiceInstance(String serviceName) {
        // 可以考虑去ServiceInstance 根据服务注册时间进行排序
        return discoveryClient.getInstances(serviceName);
    }

}
