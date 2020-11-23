package cn.com.connext.msf.server.management.webapi;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.utils.Base58UUID;
import cn.com.connext.msf.server.management.constant.ManagementAuthority;
import cn.com.connext.msf.server.management.entity.RouteApi;
import cn.com.connext.msf.server.management.service.RouteApiService;
import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import cn.com.connext.msf.webapp.annotation.ApiPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网关路由接口接口
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Api(description = "路由接口管理接口")
@ApiPath("/api/${msf.application.name}/route_api")
public class RouteApiApi {

    private final RouteApiService routeApiService;

    public RouteApiApi(RouteApiService routeApiService) {
        this.routeApiService = routeApiService;
    }

    @PostMapping
    @ApiOperation("创建信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_CREATE)
    public RouteApi create(@RequestBody RouteApi routeApi) {
        routeApi.setId(Base58UUID.newBase58UUID());
        return routeApiService.create(routeApi);
    }

    @PutMapping
    @ApiOperation("修改信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_MODIFY)
    public RouteApi modify(@RequestBody RouteApi routeApi) {
        return routeApiService.modify(routeApi);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_DELETE)
    public void delete(@PathVariable String id) {
        routeApiService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取详情")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public RouteApi findItem(@PathVariable String id) {
        return routeApiService.findItem(id);
    }

    @GetMapping
    @ApiOperation("获取分页")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public Page<RouteApi> findPage(QueryInfo queryInfo) {
        return routeApiService.findPage(queryInfo);
    }

    @GetMapping("/list")
    @ApiOperation("获取列表")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ROUTE_LOCATE)
    public List<RouteApi> findList(QueryInfo queryInfo) {
        return routeApiService.findList(queryInfo);
    }

}

