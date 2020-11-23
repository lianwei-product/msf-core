package cn.com.connext.msf.server.management.validator;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.utils.Validator;
import cn.com.connext.msf.server.management.entity.ManagedService;
import cn.com.connext.msf.server.management.repository.ManagedServiceRepository;
import org.springframework.stereotype.Component;


/**
 * 托管服务 - 实体验证定义
 * 开发人员: 程瀚
 * 修订日期: 2019-05-30 22:36:20
 */
@Component
public class ManagedServiceValidator {

    private final ManagedServiceRepository managedServiceRepository;

    /**
     * 默认实例化方法。
     *
     * @param managedServiceRepository 实体持久层
     */
    public ManagedServiceValidator(ManagedServiceRepository managedServiceRepository) {
        this.managedServiceRepository = managedServiceRepository;
    }

    /**
     * 验证所要创建的“托管服务”对象实例，是否符合数据约定。
     *
     * @param managedService “托管服务”对象实例
     */
    public void createValidate(ManagedService managedService) {
        if (managedServiceRepository.exists(managedService.getId()))
            throw new BusinessException("所要创建的“托管服务”已经存在。");

        entityValidate(managedService);
    }

    /**
     * 验证所要修改的“托管服务”对象实例，是否符合数据约定。
     *
     * @param managedService “托管服务”对象实例
     */
    public void modifyValidate(ManagedService managedService) {
        if (!managedServiceRepository.exists(managedService.getId()))
            throw new BusinessException("所要修改的“托管服务”不存在或已删除。");

        entityValidate(managedService);
    }

    /**
     * 验证所要删除的“托管服务”，是否符合数据约定。
     *
     * @param id “托管服务”对象的唯一标识
     */
    public void deleteValidate(String id) {
        if (!managedServiceRepository.exists(id))
            throw new BusinessException("所要删除的“托管服务”不存在或已删除。");
    }

    /**
     * 验证所传入的“托管服务”对象实例，是否符合数据约定。
     *
     * @param managedService “托管服务”对象实例
     */
    public void entityValidate(ManagedService managedService) {

        // 检测服务名称是否为空
        if (Validator.isEmpty(managedService.getName()))
            throw new BusinessException("“服务名称”不能为空。");

        // 检测标准实例数据是否为空
        if (Validator.isEmpty(managedService.getInstanceCount()))
            throw new BusinessException("“标准实例数据”不能为空。");

        // 检测服务名称长度
        if (Validator.isOutLimit(managedService.getName(), 50))
            throw new BusinessException("“服务名称”最大不能超过50个字符。");

        // 检测服务描述长度
        if (Validator.isOutLimit(managedService.getDescription(), 500))
            throw new BusinessException("“服务描述”最大不能超过500个字符。");

        // 检测服务名称是否已经存在
        if (managedServiceRepository.countByNameAndIdNot(managedService.getName(), managedService.getId()) > 0)
            throw new BusinessException("“服务名称”不可以重复，系统中已存在相同的记录。");

    }

}

