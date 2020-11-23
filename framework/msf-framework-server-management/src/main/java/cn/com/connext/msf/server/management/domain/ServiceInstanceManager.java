package cn.com.connext.msf.server.management.domain;

import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.server.management.entity.ServiceInstanceInfo;
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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ServiceInstanceManager {

    private static final Logger logger = LoggerFactory.getLogger(RegisteredServiceManager.class);

    public List<ServiceInstanceInfo> getServiceInstanceInfoList(List<ServiceInstance> serviceInstanceList) {
        List<ServiceInstanceInfo> instanceInfoList = new ArrayList<>();
        serviceInstanceList.forEach(instance -> {
            String url = instance.getUri().toString();
            instanceInfoList.add(ServiceInstanceInfo.from(instance, getCpuUsage(url), getMemoryUsed(url), getGcPause(url),getHealth(url)));
        });
        return instanceInfoList;
    }

    public ServiceInstanceInfo.HealthInfo getHealth(String url) {
        String serviceInstanceUrl = url + "/actuator/health";
        String resultJson = requestActuator(serviceInstanceUrl);
        return JSON.parseObject(resultJson, ServiceInstanceInfo.HealthInfo.class);
    }

    public ServiceInstanceInfo.CpuUsageInfo getCpuUsage(String url) {
        String serviceInstanceUrl = url + "/actuator/metrics/process.cpu.usage";
        String resultJson = requestActuator(serviceInstanceUrl);
        return JSON.parseObject(resultJson, ServiceInstanceInfo.CpuUsageInfo.class);
    }

    public ServiceInstanceInfo.MemoryUsedInfo getMemoryUsed(String url) {
        String serviceInstanceUrl = url + "/actuator/metrics/jvm.memory.used";
        String resultJson = requestActuator(serviceInstanceUrl);
        return JSON.parseObject(resultJson, ServiceInstanceInfo.MemoryUsedInfo.class);
    }

    public ServiceInstanceInfo.GcPauseInfo getGcPause(String url) {
        String serviceInstanceUrl = url + "/actuator/metrics/jvm.gc.pause";
        String resultJson = requestActuator(serviceInstanceUrl);
        return JSON.parseObject(resultJson, ServiceInstanceInfo.GcPauseInfo.class);
    }

    private String requestActuator(String url) {
        HttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(50000).setSocketTimeout(50000).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return "{\"status\":\"DOWN\"}";
                //throw new RuntimeException("status code is not 200.");
            }

            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            return "{\"status\":\"DOWN\"}";
            //throw new RuntimeException(e.getMessage());
        }finally {
            try {
                ((CloseableHttpClient) httpClient).close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
