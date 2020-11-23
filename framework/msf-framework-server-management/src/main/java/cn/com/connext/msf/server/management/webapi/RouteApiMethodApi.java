package cn.com.connext.msf.server.management.webapi;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.utils.Base58UUID;
import cn.com.connext.msf.server.management.constant.ManagementAuthority;
import cn.com.connext.msf.server.management.entity.RouteApiMethod;
import cn.com.connext.msf.server.management.service.RouteApiMethodService;
import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import cn.com.connext.msf.webapp.annotation.ApiPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网关路由接口方法接口
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Api(description = "路由接口方法管理接口")
@ApiPath("/api/${msf.application.name}/route_api_method")
public class RouteApiMethodApi {

    private final RouteApiMethodService routeApiMethodService;

    public RouteApiMethodApi(RouteApiMethodService routeApiMethodService) {
        this.routeApiMethodService = routeApiMethodService;
    }

    @PostMapping
    @ApiOperation("创建信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_CREATE)
    public RouteApiMethod create(@RequestBody RouteApiMethod routeApiMethod) {
        routeApiMethod.setId(Base58UUID.newBase58UUID());
        return routeApiMethodService.create(routeApiMethod);
    }

    @PutMapping
    @ApiOperation("修改信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_MODIFY)
    public RouteApiMethod modify(@RequestBody RouteApiMethod routeApiMethod) {
        return routeApiMethodService.modify(routeApiMethod);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_DELETE)
    public void delete(@PathVariable String id) {
        routeApiMethodService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取详情")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public RouteApiMethod findItem(@PathVariable String id) {
        return routeApiMethodService.findItem(id);
    }

    @GetMapping
    @ApiOperation("获取分页")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public Page<RouteApiMethod> findPage(QueryInfo queryInfo) {
        return routeApiMethodService.findPage(queryInfo);
    }

    @GetMapping("/list")
    @ApiOperation("获取列表")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public List<RouteApiMethod> findList(QueryInfo queryInfo) {
        return routeApiMethodService.findList(queryInfo);
    }

}

