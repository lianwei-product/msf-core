package cn.com.connext.msf.server.management.domain;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.server.management.entity.ZonedServiceSetting;
import cn.com.connext.msf.server.management.model.ZonedServiceInstance;
import cn.com.connext.msf.server.management.model.ZonedServiceRevisePlan;
import cn.com.connext.msf.server.management.repository.ServiceZoneSettingRepository;
import cn.com.connext.msf.server.management.validator.ZonedServiceSettingValidator;
import cn.com.connext.msf.server.management.view.ZonedServiceSettingDataView;
import cn.com.connext.msf.server.management.view.ZonedServiceSettingListView;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 对象说明：服务可用区配置管理器
 * 开发人员：程瀚
 * 摘要说明：用于根据“服务可用区配置”，动态调度相关微服务的可用区，从而实现不同可用区间的流量隔离。
 * 修订日期：2020-6-17 9:50:31
 */
@Component
public class ZonedServiceSettingManager {

    private final ZonedServiceSettingValidator validator;
    private final ServiceZoneSettingRepository repository;
    private final ZonedServiceInstanceManager zonedServiceInstanceManager;

    public ZonedServiceSettingManager(ZonedServiceSettingValidator validator, ServiceZoneSettingRepository repository, ZonedServiceInstanceManager zonedServiceInstanceManager) {
        this.validator = validator;
        this.repository = repository;
        this.zonedServiceInstanceManager = zonedServiceInstanceManager;
    }

    public void apply() {
        List<ZonedServiceSetting> settings = repository.findList();
        settings.forEach(this::apply);
    }

    public void apply(String id) {
        ZonedServiceSetting setting = repository.findItem(id);
        if (setting == null) {
            return;
        }
        apply(setting);
    }

    private void apply(ZonedServiceSetting setting) {
        int minInstanceCount = setting.getMinInstanceCount();
        if (minInstanceCount == 0) {
            return;
        }

        List<ZonedServiceInstance> instances = zonedServiceInstanceManager.getInstances(setting.getName());
        ZonedServiceRevisePlanBuilder builder = new ZonedServiceRevisePlanBuilder(setting, instances);
        List<ZonedServiceRevisePlan> revisePlans = builder.buildRevisePlans();
        revisePlans.forEach(zonedServiceInstanceManager::revise);
    }

    public ZonedServiceSettingDataView findView(String id) {
        ZonedServiceSetting setting = repository.findItem(id);
        if (setting == null) {
            return null;
        }

        List<ZonedServiceInstance> instances = zonedServiceInstanceManager.getInstances(setting.getName());
        ZonedServiceRevisePlanBuilder builder = new ZonedServiceRevisePlanBuilder(setting, instances);
        return ZonedServiceSettingDataView.from(setting, builder.getStatus());
    }

    public Page<ZonedServiceSettingListView> findPage(QueryInfo queryInfo) {
        Page<ZonedServiceSetting> dataPage = repository.findPage(queryInfo);
        List<ZonedServiceSettingListView> content = Lists.newArrayList();
        dataPage.getContent().forEach(setting -> {
            List<ZonedServiceInstance> instances = zonedServiceInstanceManager.getInstances(setting.getName());
            ZonedServiceRevisePlanBuilder builder = new ZonedServiceRevisePlanBuilder(setting, instances);
            content.add(ZonedServiceSettingListView.from(setting, builder.getStatus()));
        });
        return new PageImpl<>(content, dataPage.getPageable(), dataPage.getTotalElements());
    }

    public ZonedServiceSetting create(ZonedServiceSetting zonedServiceSetting) {
        validator.createValidate(zonedServiceSetting);
        return repository.create(zonedServiceSetting);
    }

    public ZonedServiceSetting modify(ZonedServiceSetting zonedServiceSetting) {
        validator.modifyValidate(zonedServiceSetting);
        return repository.modify(zonedServiceSetting);
    }

    public void delete(String id) {
        validator.deleteValidate(id);
        repository.delete(id);
    }

    public ZonedServiceSetting findItem(String id) {
        return repository.findOne(id);
    }


}
