package cn.com.connext.msf.framework.management;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.route.service.ServiceRoute;
import cn.com.connext.msf.framework.utils.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;

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
