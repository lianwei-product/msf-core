package cn.com.connext.msf.server.management.view;

import cn.com.connext.msf.server.management.entity.ZonedServiceSetting;
import cn.com.connext.msf.server.management.model.ZoneStatus;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

public class ZonedServiceSettingDataView {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @Indexed(unique = true)
    @ApiModelProperty(value = "服务名称")
    private String name;

    @ApiModelProperty(value = "服务别名")
    private String aliasName;

    @ApiModelProperty(value = "服务描述")
    private String description;

    @ApiModelProperty(value = "可用区状态信息")
    private List<ZoneStatus> zoneStatusList;

    public static ZonedServiceSettingDataView from(ZonedServiceSetting setting, List<ZoneStatus> zoneStatusList) {
        ZonedServiceSettingDataView view = new ZonedServiceSettingDataView();
        view.id = setting.getId();
        view.name = setting.getName();
        view.aliasName = setting.getAliasName();
        view.description = setting.getDescription();
        view.zoneStatusList = zoneStatusList;
        return view;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public String getDescription() {
        return description;
    }

    public List<ZoneStatus> getZoneStatusList() {
        return zoneStatusList;
    }
}
