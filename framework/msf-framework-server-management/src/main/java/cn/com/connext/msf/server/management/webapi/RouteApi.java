package cn.com.connext.msf.server.management.webapi;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.utils.Base58UUID;
import cn.com.connext.msf.server.management.constant.ManagementAuthority;
import cn.com.connext.msf.server.management.domain.RouteManager;
import cn.com.connext.msf.server.management.entity.Route;
import cn.com.connext.msf.server.management.service.RouteService;
import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import cn.com.connext.msf.webapp.annotation.ApiPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网关路由接口
 * 开发人员: 程瀚
 * 修订日期: 2019-06-02 20:29:56
 */
@Api(description = "路由管理接口")
@ApiPath("/api/${msf.application.name}/route")
@SuppressWarnings("unused")
public class RouteApi {

    private final RouteManager routeManager;
    private final RouteService routeService;

    public RouteApi(RouteManager routeManager,
                    RouteService routeService) {
        this.routeManager = routeManager;
        this.routeService = routeService;
    }

    @PostMapping
    @ApiOperation("创建信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_CREATE)
    public Route registerFromService(@RequestBody Route route) {
        route.setId(Base58UUID.newBase58UUID());
        return routeService.create(route);
    }

    @PutMapping
    @ApiOperation("修改信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_MODIFY)
    public Route modify(@RequestBody Route route) {
        return routeService.modify(route);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_DELETE)
    public void delete(@PathVariable String id) {
        routeService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取详情")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public Route findItem(@PathVariable String id) {
        return routeService.findItem(id);
    }

    @GetMapping
    @ApiOperation("获取分页")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public Page<Route> findPage(QueryInfo queryInfo) {
        return routeService.findPage(queryInfo);
    }

    @GetMapping("/list")
    @ApiOperation("获取列表")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public List<Route> findList(QueryInfo queryInfo) {
        return routeService.findList(queryInfo);
    }


    @PostMapping("/register/{serviceName}")
    @ApiOperation("注册路由")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_CREATE)
    public Route registerFromService(@PathVariable String serviceName, @RequestParam(defaultValue = "false") boolean overwrite) {
        return routeManager.registerFromService(serviceName, overwrite);
    }
}

