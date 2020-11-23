package cn.com.connext.msf.microservice.zone;

import cn.com.connext.msf.framework.utils.JSON;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

/**
 * 服务可用区管理器
 * 开发人员：程瀚
 * 摘要说明：提供可用区初始化、查询、修改等功能，当可用区变更时，会将最新的可用区信息注册到Eureka服务器。
 * 修订日期：2020-6-17 8:51:18
 */
public class ServiceZoneManager implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(ServiceZoneManager.class);

    private final EurekaClient eurekaClient;
    private final ServiceZone serviceZone;

    public ServiceZoneManager(EurekaClient eurekaClient,
                              @Value("${msf.service-zone.zone:common}") String zone,
                              @Value("${msf.service-zone.available-zones:*}") String availableZonesString) {
        this.eurekaClient = eurekaClient;
        this.serviceZone = ServiceZone.from(zone, availableZonesString);
    }

    @Override
    public void run(String... args) throws Exception {
        initialZone();
    }

    public ServiceZone current() {
        return serviceZone;
    }

    public void initialZone() {
        logger.info("Starting initial service zone: {}", JSON.toJsonString(serviceZone));
        registerToEurekaServer();
    }

    public void changeZone(String zone, String availableZonesString) {
        ServiceZone newServiceZone = ServiceZone.from(zone, availableZonesString);
        String before = JSON.toJsonString(serviceZone);
        serviceZone.update(newServiceZone.getZone(), newServiceZone.getAvailableZones());
        logger.info("Service zone changed, {} -> {}", before, JSON.toJsonString(serviceZone));
        registerToEurekaServer();
    }

    public boolean isSameZone(String zone) {
        return Objects.equals(serviceZone.getZone(), zone);
    }

    public boolean canAccessTargetZone(String zone) {
        return CollectionUtils.isEmpty(serviceZone.getAvailableZones())
                || serviceZone.getAvailableZones().contains("*")
                || serviceZone.getAvailableZones().contains(zone);
    }

    private void registerToEurekaServer() {
        eurekaClient.getApplicationInfoManager().getInfo().getMetadata().put("service-zone", serviceZone.getZone());
        eurekaClient.getApplicationInfoManager().getInfo().setIsDirty();
    }

}
