package cn.com.connext.msf.server.management.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 对象说明：服务可用区配置模板
 * 开发人员：程瀚
 * 摘要说明：
 * 修订日期：2020-06-16 15:34:39
 */
public class ZonedServiceTemplate {

    @ApiModelProperty(value = "可用区名称", required = true)
    private String zone;

    @ApiModelProperty(value = "可以访问的可用区，多个可用区采用逗号分隔", required = true)
    private String availableZonesString;

    @ApiModelProperty(value = "最小实例数量", required = true)
    private int minCount;

    private ZonedServiceTemplate() {
    }

    public static ZonedServiceTemplate from(String zone, String availableZonesString, int minCount) {
        ZonedServiceTemplate template = new ZonedServiceTemplate();
        template.zone = zone;
        template.availableZonesString = availableZonesString;
        template.minCount = minCount;
        return template;
    }

    public String getZone() {
        return zone;
    }

    public String getAvailableZonesString() {
        return availableZonesString;
    }

    public int getMinCount() {
        return minCount;
    }
}
