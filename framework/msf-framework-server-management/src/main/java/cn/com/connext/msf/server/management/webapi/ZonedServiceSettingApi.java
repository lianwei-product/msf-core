package cn.com.connext.msf.server.management.webapi;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.server.management.constant.ManagementAuthority;
import cn.com.connext.msf.server.management.domain.ZonedServiceSettingManager;
import cn.com.connext.msf.server.management.entity.ZonedServiceSetting;
import cn.com.connext.msf.server.management.view.ZonedServiceSettingDataView;
import cn.com.connext.msf.server.management.view.ZonedServiceSettingListView;
import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import cn.com.connext.msf.webapp.annotation.ApiPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(description = "服务可用区配置管理接口")
@ApiPath("/api/${msf.application.name}/zoned_service_setting")
public class ZonedServiceSettingApi {

    private final ZonedServiceSettingManager zonedServiceSettingManager;

    public ZonedServiceSettingApi(ZonedServiceSettingManager zonedServiceSettingManager) {
        this.zonedServiceSettingManager = zonedServiceSettingManager;
    }

    @PostMapping
    @ApiOperation("创建信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ZONE_CREATE)
    public ZonedServiceSetting create(@RequestBody ZonedServiceSetting zonedServiceSetting) {
        return zonedServiceSettingManager.create(zonedServiceSetting);
    }

    @PutMapping
    @ApiOperation("修改信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ZONE_MODIFY)
    public ZonedServiceSetting modify(@RequestBody ZonedServiceSetting zonedServiceSetting) {
        return zonedServiceSettingManager.modify(zonedServiceSetting);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除信息")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ZONE_DELETE)
    public void delete(@PathVariable String id) {
        zonedServiceSettingManager.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取详情")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ZONE_LOCATE)
    public ZonedServiceSetting findItem(@PathVariable String id) {
        return zonedServiceSettingManager.findItem(id);
    }

    @GetMapping("/{id}/data_view")
    @ApiOperation("获取视图")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ZONE_LOCATE)
    public ZonedServiceSettingDataView findView(@PathVariable String id) {
        return zonedServiceSettingManager.findView(id);
    }

    @GetMapping
    @ApiOperation("获取分页")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ZONE_LOCATE)
    public Page<ZonedServiceSettingListView> findPage(QueryInfo queryInfo) {
        return zonedServiceSettingManager.findPage(queryInfo);
    }

    @PutMapping("/apply")
    @ApiOperation("应用所有配置")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ZONE_MODIFY)
    public void apply() {
        zonedServiceSettingManager.apply();
    }

    @PutMapping("/apply/{id}")
    @ApiOperation("应用指定配置")
    @ApiAuthority(ManagementAuthority.MANAGEMENT_ZONE_MODIFY)
    public void apply(@PathVariable String id) {
        zonedServiceSettingManager.apply(id);
    }
}
