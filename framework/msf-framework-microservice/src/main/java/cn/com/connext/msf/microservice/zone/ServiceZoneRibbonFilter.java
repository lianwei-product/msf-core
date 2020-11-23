package cn.com.connext.msf.microservice.zone;

import com.google.common.collect.Lists;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAffinityServerListFilter;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 服务可用区Ribbon服务实例过滤器
 * 开发人员：程瀚
 * 摘要说明：该过滤器优先使用返回同一可用区的服务实例，当同一可用区没有可用实例时，会返回当前实例可访问的其它可用区内的实例。
 * 修订日期：2020-6-17 8:57:14
 */
public class ServiceZoneRibbonFilter extends ZoneAffinityServerListFilter<Server> {

    private final ServiceZoneManager serviceZoneManager;

    public ServiceZoneRibbonFilter(ServiceZoneManager serviceZoneManager) {
        this.serviceZoneManager = serviceZoneManager;
        LoggerFactory.getLogger(ServiceZoneRibbonFilter.class).info("Init ServiceZoneRibbonFilter, availableZones: {}",
                StringUtils.join(serviceZoneManager.current().getAvailableZones(), ","));
    }


    @Override
    public List<Server> getFilteredListOfServers(List<Server> allServers) {
        List<Server> servers = super.getFilteredListOfServers(allServers);
        List<Server> sameZoneServers = Lists.newArrayList();
        List<Server> availableServers = Lists.newArrayList();

        for (Server server : servers) {
            DiscoveryEnabledServer enabledServer = (DiscoveryEnabledServer) server;
            String serverZone = enabledServer.getInstanceInfo().getMetadata().getOrDefault("service-zone", "common");
            if (serviceZoneManager.isSameZone(serverZone)) {
                sameZoneServers.add(enabledServer);
            }

            if (serviceZoneManager.canAccessTargetZone(serverZone)) {
                availableServers.add(server);
            }
        }

        return sameZoneServers.size() > 0 ? sameZoneServers : availableServers;
    }

}
