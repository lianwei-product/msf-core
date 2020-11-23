package cn.com.connext.msf.server.management.view;

import cn.com.connext.msf.server.management.entity.ZonedServiceSetting;
import cn.com.connext.msf.server.management.entity.ZonedServiceTemplate;
import cn.com.connext.msf.server.management.model.ZoneStatus;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ZonedServiceSettingListView {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @Indexed(unique = true)
    @ApiModelProperty(value = "服务名称")
    private String name;

    @ApiModelProperty(value = "服务别名")
    private String aliasName;

    @ApiModelProperty(value = "标准实例数量")
    private int minCount;

    @ApiModelProperty(value = "当前实例数量")
    private int curCount;

    @ApiModelProperty(value = "空闲实例数量")
    private int freeCount;

    @ApiModelProperty(value = "状态")
    private String status;

    public static ZonedServiceSettingListView from(ZonedServiceSetting setting, List<ZoneStatus> statusList) {
        Set<String> zoneSet = setting.getTemplates().stream().map(ZonedServiceTemplate::getZone).collect(Collectors.toSet());

        ZonedServiceSettingListView view = new ZonedServiceSettingListView();
        view.id = setting.getId();
        view.name = setting.getName();
        view.aliasName = setting.getAliasName();
        view.minCount = setting.getMinInstanceCount();

        statusList.forEach(status -> {
            if (zoneSet.contains(status.getZone())) {
                if (status.getCurCount() <= status.getMinCount()) {
                    view.curCount += status.getCurCount();
                } else {
                    view.curCount += status.getMinCount();
                    view.freeCount += status.getCurCount() - status.getMinCount();
                }
            } else {
                view.freeCount += status.getCurCount();
            }
        });
        view.status = view.minCount <= view.curCount ? "运行正常" : "实例不足";
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

    public int getMinCount() {
        return minCount;
    }

    public int getCurCount() {
        return curCount;
    }

    public int getFreeCount() {
        return freeCount;
    }

    public String getStatus() {
        return status;
    }
}
