package cn.com.connext.msf.server.management.domain;

import cn.com.connext.msf.framework.exception.BusinessException;

import cn.com.connext.msf.framework.utils.RestClient;
import cn.com.connext.msf.server.management.model.ZonedServiceInstance;
import cn.com.connext.msf.server.management.model.ZonedServiceRevisePlan;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.text.MessageFormat;
import java.util.List;

/**
 * 对象说明：服务实例管理器
 * 开发人员：程瀚
 * 摘要说明：提供微服务实例获取、可用区调整功能
 * 修订日期：2020-06-18 16:24:25
 */
@Component
public class ZonedServiceInstanceManager {

    private final Logger logger = LoggerFactory.getLogger(ZonedServiceInstanceManager.class);
    private final RestClient restClient;
    private final EurekaDiscoveryClient discoveryClient;

    public ZonedServiceInstanceManager(EurekaDiscoveryClient discoveryClient) {
        this.restClient = new RestClient();
        this.discoveryClient = discoveryClient;
    }

    public List<ZonedServiceInstance> getInstances(String name) {
        List<ZonedServiceInstance> instances = Lists.newArrayList();
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(name);
        for (ServiceInstance serviceInstance : serviceInstances) {
            ZonedServiceInstance instance = ZonedServiceInstance.from(serviceInstance);
            if (validate(instance)) {
                instances.add(instance);
            }
        }
        return instances;
    }

    public void revise(ZonedServiceRevisePlan plan) {
        String url = plan.getUri().concat("/actuator/service-zone");
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Content-Type", "application/json");

        ObjectNode request = JsonNodeFactory.instance.objectNode();
        request.put("zone", plan.getTargetZone());
        request.put("availableZonesString", plan.getTargetAvailableZonesString());

        try {
            restClient.post(url, header, request);
        } catch (Exception e) {
            String message;
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException he = (HttpClientErrorException) e;
                message = MessageFormat.format("Revise service instance zone error, instanceId: {0}, url: {1}, response: {2}", plan.getId(), url, he.getResponseBodyAsString());
            } else {
                message = MessageFormat.format("Revise service instance zone error, instanceId: {0}, url: {1}", plan.getId(), url);
            }
            logger.error(message, e);
            throw new BusinessException(message);
        }
    }

    private boolean validate(ZonedServiceInstance instance) {
        String url = instance.getUri().concat("/actuator/service-zone");
        try {
            ObjectNode objectNode = restClient.get(url, ObjectNode.class);
            String zone = objectNode.get("zone").asText("common");
            instance.setZone(zone);
            return true;
        } catch (Exception e) {
            logger.error("Get service instance info error, instanceId: {}, url: {}", instance.getId(), url, e);
            return false;
        }
    }

}
