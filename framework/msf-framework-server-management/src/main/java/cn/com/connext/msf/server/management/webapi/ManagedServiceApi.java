package cn.com.connext.msf.server.management.webapi;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.utils.Base58UUID;
import cn.com.connext.msf.server.management.constant.ManagementAuthority;
import cn.com.connext.msf.server.management.domain.ManagedServiceTaskManager;
import cn.com.connext.msf.server.management.entity.ManagedService;
import cn.com.connext.msf.server.management.entity.ManagedServiceView;
import cn.com.connext.msf.server.management.service.ManagedServiceService;
import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import cn.com.connext.msf.webapp.annotation.ApiPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 托管服务接口
 * 开发人员: 程瀚
 * 修订日期: 2019-05-30 22:36:20
 */
@Api(description = "托管服务接口")
@ApiPath("/api/${msf.application.name}/managed_service")
public class ManagedServiceApi {

    private final ManagedServiceService managedServiceService;
    private final ManagedServiceTaskManager managedServiceTaskManager;

    public ManagedServiceApi(ManagedServiceService managedServiceService,
                             ManagedServiceTaskManager managedServiceTaskManager) {
        this.managedServiceService = managedServiceService;
        this.managedServiceTaskManager = managedServiceTaskManager;
    }

    @PostMapping
    @ApiOperation("创建信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_CREATE)
    public ManagedService create(@RequestBody ManagedService managedService) {
        managedService.setId(Base58UUID.newBase58UUID());
        managedServiceService.create(managedService);
        managedServiceTaskManager.create();
        return managedService;
    }

    @PutMapping
    @ApiOperation("修改信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_MODIFY)
    public ManagedService modify(@RequestBody ManagedService managedService) {
        return managedServiceService.modify(managedService);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_DELETE)
    public void delete(@PathVariable String id) {
        managedServiceService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取详情")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_LOCATE)
    public ManagedService findItem(@PathVariable String id) {
        return managedServiceService.findItem(id);
    }

    @GetMapping
    @ApiOperation("获取分页")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_LOCATE)
    public Page<ManagedService> findPage(QueryInfo queryInfo) {
        return managedServiceService.findPage(queryInfo);
    }

    @GetMapping("/list")
    @ApiOperation("获取列表")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_LOCATE)
    public List<ManagedService> findList(QueryInfo queryInfo) {
        return managedServiceService.findList(queryInfo);
    }

    @GetMapping("/view/{id}")
    @ApiOperation("获取视图详情")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_LOCATE)
    public ManagedServiceView findViewItem(@PathVariable String id) {
        return managedServiceService.findViewItem(id);
    }

    @GetMapping("page_view")
    @ApiOperation("获取视图分页")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_LOCATE)
    public Page<ManagedServiceView> findViewPage(QueryInfo queryInfo) {
        return managedServiceService.findViewPage(queryInfo);
    }

    @GetMapping("manual_listen")
    @ApiOperation("手动监听服务")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_SERVICE_LOCATE)
    public void checkServiceHealth() {
        managedServiceService.checkServiceHealth();
    }

}

