package cn.com.connext.msf.server.management.domain;

import cn.com.connext.msf.server.management.entity.ZonedServiceSetting;
import cn.com.connext.msf.server.management.entity.ZonedServiceTemplate;
import cn.com.connext.msf.server.management.model.ZoneStatus;
import cn.com.connext.msf.server.management.model.ZonedServiceInstance;
import cn.com.connext.msf.server.management.model.ZonedServiceRevisePlan;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 对象说明：服务可用区调整方案构造器
 * 开发人员：程瀚
 * 摘要说明：根据“服务可用区配置”，以及当前可用区类服务实例状态，构造服务实例的可用区调整方案。
 * 修订日期：2020-06-18 10:09:49
 */
public class ZonedServiceRevisePlanBuilder {

    private final Map<String, InstanceCounter> map;
    private final List<ZonedServiceInstance> commonInstances;
    private final List<ZonedServiceInstance> invalidInstances;

    public ZonedServiceRevisePlanBuilder(ZonedServiceSetting setting, List<ZonedServiceInstance> instances) {
        map = Maps.newLinkedHashMap();
        setting.getTemplates().forEach(template -> {
            map.put(template.getZone(), InstanceCounter.init(template));
        });
        commonInstances = Lists.newArrayList();
        invalidInstances = Lists.newArrayList();
        instances.forEach(this::append);
    }

    public List<ZonedServiceRevisePlan> buildRevisePlans() {
        List<ZonedServiceRevisePlan> revisePlans = Lists.newArrayList();
        map.forEach((zone, instanceCounter) -> {
            invalidInstances.addAll(instanceCounter.takeExceed());
        });

        map.forEach((zone, instanceCounter) -> {
            while (instanceCounter.needAppendInstance() && hasFreeInstance()) {
                ZonedServiceInstance instance = takeFreeInstance();
                instanceCounter.appendInstance(instance);
                revisePlans.add(ZonedServiceRevisePlan.from(instance, instanceCounter.template));
            }
        });

        while (invalidInstances.size() > 0) {
            ZonedServiceInstance instance = invalidInstances.remove(0);
            commonInstances.add(instance);
            revisePlans.add(ZonedServiceRevisePlan.from(instance, "common", "*"));
        }

        return revisePlans;
    }

    public List<ZoneStatus> getStatus() {
        List<ZoneStatus> list = Lists.newArrayList();
        map.forEach((zone, instanceCounter) -> {
            list.add(ZoneStatus.from(zone, instanceCounter.minCount, instanceCounter.instances));
        });
        if (commonInstances.size() > 0) {
            list.add(ZoneStatus.from("common", 0, commonInstances));
        }

        if (invalidInstances.size() > 0) {
            list.add(ZoneStatus.from("invalid", 0, invalidInstances));
        }
        return list;
    }

    private void append(ZonedServiceInstance instance) {
        InstanceCounter instanceCounter = map.get(instance.getZone());
        if (instanceCounter != null) {
            instanceCounter.curCount++;
            instanceCounter.instances.add(instance);
            return;
        }

        if (Objects.equals("common", instance.getZone())) {
            commonInstances.add(instance);
            return;
        }

        invalidInstances.add(instance);
    }

    private boolean hasFreeInstance() {
        return commonInstances.size() > 0 || invalidInstances.size() > 0;
    }

    private ZonedServiceInstance takeFreeInstance() {
        return invalidInstances.size() > 0 ? invalidInstances.remove(0) : commonInstances.remove(0);
    }


    static class InstanceCounter {

        private int minCount;
        private int curCount;
        private ZonedServiceTemplate template;
        private List<ZonedServiceInstance> instances;

        static InstanceCounter init(ZonedServiceTemplate template) {
            InstanceCounter instanceCounter = new InstanceCounter();
            instanceCounter.minCount = template.getMinCount();
            instanceCounter.curCount = 0;
            instanceCounter.template = template;
            instanceCounter.instances = Lists.newArrayList();
            return instanceCounter;
        }

        /**
         * 移除并返回超过minCount的服务实例
         */
        List<ZonedServiceInstance> takeExceed() {
            List<ZonedServiceInstance> exceedInstances = Lists.newArrayList();
            while (curCount > minCount) {
                exceedInstances.add(instances.remove(instances.size() - 1));
                curCount = instances.size();
            }
            return exceedInstances;
        }

        boolean needAppendInstance() {
            return curCount < minCount;
        }

        void appendInstance(ZonedServiceInstance instance) {
            instances.add(instance);
            curCount = instances.size();
        }
    }
}
