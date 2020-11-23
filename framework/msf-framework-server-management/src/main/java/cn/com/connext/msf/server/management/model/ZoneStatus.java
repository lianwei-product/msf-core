package cn.com.connext.msf.server.management.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 对象说明：可用区状态信息
 * 开发人员：程瀚
 * 摘要说明：
 * 修订日期：2020-06-18 16:33:32
 */
public class ZoneStatus {

    @ApiModelProperty(value = "可用区名称")
    private String zone;

    @ApiModelProperty(value = "最小实例数量")
    private int minCount;

    @ApiModelProperty(value = "当前实例数量")
    private int curCount;

    @ApiModelProperty(value = "服务实例列表")
    private List<ZonedServiceInstance> instances;

    public static ZoneStatus from(String zone, int minCount, List<ZonedServiceInstance> instances) {
        ZoneStatus status = new ZoneStatus();
        status.zone = zone;
        status.minCount = minCount;
        status.curCount = instances.size();
        status.instances = instances;
        return status;
    }

    public String getZone() {
        return zone;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getCurCount() {
        return curCount;
    }

    public List<ZonedServiceInstance> getInstances() {
        return instances;
    }
}
