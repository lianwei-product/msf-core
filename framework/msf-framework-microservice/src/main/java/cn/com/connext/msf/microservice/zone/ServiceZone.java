package cn.com.connext.msf.microservice.zone;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * 服务可用区信息
 * 开发人员：程瀚
 * 摘要说明：一个微服务属于1个（且只属于1个）可用区，但可以访问多个可用区。
 * 修订日期：2020-6-17 8:47:41
 */
public class ServiceZone {

    /**
     * 当前服务实例所属可用区
     */
    private String zone;

    /**
     * 当前服务实例可以访问的可用区，当该集合为空时，表示可以访问所有可用区。
     */
    private Set<String> availableZones;

    private ServiceZone() {
    }

    public static ServiceZone from(String zone, String availableZonesString) {
        Set<String> availableZones = Sets.newHashSet();
        try {
            availableZones = Sets.newHashSet(StringUtils.split(availableZonesString, ","));
        } catch (Exception e) {
            // do noting.
        }

        ServiceZone serviceZone = new ServiceZone();
        serviceZone.update(zone, availableZones);
        return serviceZone;
    }


    public void update(String zone, Set<String> availableZones) {
        this.zone = StringUtils.isEmpty(zone) ? "common" : zone;
        this.availableZones = availableZones == null ? Sets.newHashSet() : availableZones;
    }

    public String getZone() {
        return zone;
    }

    public Set<String> getAvailableZones() {
        return availableZones;
    }
}
