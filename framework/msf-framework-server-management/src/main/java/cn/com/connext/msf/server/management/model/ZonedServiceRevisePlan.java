package cn.com.connext.msf.server.management.model;


import cn.com.connext.msf.server.management.entity.ZonedServiceTemplate;

/**
 * 对象说明：服务实例可用区调整计划
 * 开发人员：程瀚
 * 摘要说明：
 * 修订日期：2020-06-18 16:27:06
 */
public class ZonedServiceRevisePlan {

    private String id;
    private String uri;
    private String sourceZone;
    private String targetZone;
    private String targetAvailableZonesString;

    public static ZonedServiceRevisePlan from(ZonedServiceInstance instance, ZonedServiceTemplate template) {
        return from(instance, template.getZone(), template.getAvailableZonesString());
    }

    public static ZonedServiceRevisePlan from(ZonedServiceInstance instance, String targetZone, String targetAvailableZonesString) {
        ZonedServiceRevisePlan plan = new ZonedServiceRevisePlan();
        plan.id = instance.getId();
        plan.uri = instance.getUri();
        plan.sourceZone = instance.getZone();
        plan.targetZone = targetZone;
        plan.targetAvailableZonesString = targetAvailableZonesString;
        return plan;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getSourceZone() {
        return sourceZone;
    }

    public String getTargetZone() {
        return targetZone;
    }

    public String getTargetAvailableZonesString() {
        return targetAvailableZonesString;
    }
}
