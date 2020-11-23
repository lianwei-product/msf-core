package cn.com.connext.msf.server.management.validator;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.utils.Validator;
import cn.com.connext.msf.server.management.entity.ZonedServiceSetting;
import cn.com.connext.msf.server.management.repository.ServiceZoneSettingRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 服务可用区配置 - 实体验证定义
 * 开发人员: 程瀚
 * 修订日期: 2020-06-18 14:56:03
 */
@Component
public class ZonedServiceSettingValidator {

    private final ServiceZoneSettingRepository serviceZoneSettingRepository;

    /**
     * 默认实例化方法。
     *
     * @param serviceZoneSettingRepository 实体持久层
     */
    public ZonedServiceSettingValidator(ServiceZoneSettingRepository serviceZoneSettingRepository) {
        this.serviceZoneSettingRepository = serviceZoneSettingRepository;
    }

    /**
     * 验证所要创建的“服务可用区配置”对象实例，是否符合数据约定。
     *
     * @param zonedServiceSetting “服务可用区配置”对象实例
     */
    public void createValidate(ZonedServiceSetting zonedServiceSetting) {
        if (serviceZoneSettingRepository.exists(zonedServiceSetting.getId()))
            throw new BusinessException("所要创建的“服务可用区配置”已经存在。");

        entityValidate(zonedServiceSetting);
    }

    /**
     * 验证所要修改的“服务可用区配置”对象实例，是否符合数据约定。
     *
     * @param zonedServiceSetting “服务可用区配置”对象实例
     */
    public void modifyValidate(ZonedServiceSetting zonedServiceSetting) {
        if (!serviceZoneSettingRepository.exists(zonedServiceSetting.getId()))
            throw new BusinessException("所要修改的“服务可用区配置”不存在或已删除。");

        entityValidate(zonedServiceSetting);
    }

    /**
     * 验证所要删除的“服务可用区配置”，是否符合数据约定。
     *
     * @param id “服务可用区配置”对象的唯一标识
     */
    public void deleteValidate(String id) {
        if (!serviceZoneSettingRepository.exists(id))
            throw new BusinessException("所要删除的“服务可用区配置”不存在或已删除。");
    }

    /**
     * 验证所传入的“服务可用区配置”对象实例，是否符合数据约定。
     *
     * @param zonedServiceSetting “服务可用区配置”对象实例
     */
    public void entityValidate(ZonedServiceSetting zonedServiceSetting) {

        // 检测服务名称是否为空
        if (Validator.isEmpty(zonedServiceSetting.getName()))
            throw new BusinessException("“服务名称”不能为空。");

        if (zonedServiceSetting.getTemplates().size() == 0)
            throw new BusinessException("“可用区模板”不能为空。");

        // 检测服务名称是否已经存在
        if (serviceZoneSettingRepository.countByNameAndIdNot(zonedServiceSetting.getName(), zonedServiceSetting.getId()) > 0)
            throw new BusinessException("“服务名称”不可以重复，系统中已存在相同的记录。");

        zonedServiceSetting.getTemplates().forEach(template -> {
            if (template.getMinCount() <= 0) {
                throw new BusinessException("可用区“最小实例数量”必须大于0。");
            }

            if (Objects.equals("common", template.getZone())) {
                throw new BusinessException("“可用区名称”不可以为“common”。");
            }

            if (Objects.equals("invalid", template.getZone())) {
                throw new BusinessException("“可用区名称”不可以为“invalid”。");
            }

        });

    }

}
