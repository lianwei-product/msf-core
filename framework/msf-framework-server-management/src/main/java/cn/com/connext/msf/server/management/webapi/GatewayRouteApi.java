package cn.com.connext.msf.server.management.webapi;

import cn.com.connext.msf.framework.route.gateway.GatewayRoute;
import cn.com.connext.msf.server.management.domain.GatewayRouteManager;
import cn.com.connext.msf.webapp.annotation.ApiPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 网关路由接口
 * 开发人员: 程瀚
 * 修订日期: 2019-06-02 21:10:58
 */
@Api(description = "网关路由刷新接口")
@ApiPath("/api/${msf.application.name}/gateway_route")
public class GatewayRouteApi {

    private final GatewayRouteManager gatewayRouteManager;

    public GatewayRouteApi(GatewayRouteManager gatewayRouteManager) {
        this.gatewayRouteManager = gatewayRouteManager;
    }

    @GetMapping("/{id}")
    @ApiOperation("获取详情")
    public GatewayRoute findItem(@PathVariable String id) {
        return gatewayRouteManager.findItem(id);
    }

    @GetMapping
    @ApiOperation("获取列表")
    public List<GatewayRoute> findList() {
        return gatewayRouteManager.findList();
    }


}

