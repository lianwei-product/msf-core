package cn.com.connext.msf.server.gateway.client;

import cn.com.connext.msf.framework.route.gateway.GatewayRoute;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${msf.management.service-name}", path = "/api/${msf.management.service-name}/gateway_route")
public interface GatewayRouteClient {

    @GetMapping("/{id}")
    GatewayRoute findItem(@PathVariable(name = "id") String id);

    @GetMapping
    List<GatewayRoute> findList();

}

