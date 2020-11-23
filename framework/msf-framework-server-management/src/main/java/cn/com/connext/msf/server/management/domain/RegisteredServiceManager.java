package cn.com.connext.msf.server.management.domain;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.management.BaseServiceManager;
import cn.com.connext.msf.framework.route.service.ServiceRoute;
import cn.com.connext.msf.framework.utils.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 已注册服务管理器
 */
@Component
public class RegisteredServiceManager extends BaseServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(RegisteredServiceManager.class);
    private final String ERROR_MESSAGE = "获取服务路由信息失败，指定服务可能尚未提供路由获取接口，或服务已下线。";

    public RegisteredServiceManager(EurekaDiscoveryClient discoveryClient) {
        super(discoveryClient);
    }

    public ServiceRoute findServiceRoute(String serviceName) {
        List<ServiceInstance> serviceInstanceList = findServiceInstance(serviceName);
        if (serviceInstanceList.size() == 0) {
            throw new BusinessException(ERROR_MESSAGE);
        }

        ServiceInstance serviceInstance = serviceInstanceList.get(0);
        String serviceRouteUrl = serviceInstance.getUri().toString() + "/actuator/service-route";
        return requestServiceRoute(serviceRouteUrl);
    }

    private ServiceRoute requestServiceRoute(String url) {
        HttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setSocketTimeout(5000).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("status code is not 200.");
            }

            String serviceRouteJson = EntityUtils.toString(response.getEntity(), "utf-8");
            ServiceRoute serviceRoute = JSON.parseObject(serviceRouteJson, ServiceRoute.class);
            if (serviceRoute == null || serviceRoute.getServiceName() == null) {
                throw new RuntimeException("service route is empty.");
            }

            return serviceRoute;
        } catch (Exception e) {
            throw new BusinessException(ERROR_MESSAGE);
        } finally {
            try {
                ((CloseableHttpClient) httpClient).close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
