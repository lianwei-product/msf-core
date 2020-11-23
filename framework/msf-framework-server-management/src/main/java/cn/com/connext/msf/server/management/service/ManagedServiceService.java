package cn.com.connext.msf.server.management.service;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.utils.Validator;
import cn.com.connext.msf.server.management.domain.RegisteredServiceManager;
import cn.com.connext.msf.server.management.domain.ServiceInstanceManager;
import cn.com.connext.msf.server.management.entity.ManagedService;
import cn.com.connext.msf.server.management.entity.ManagedServiceView;
import cn.com.connext.msf.server.management.entity.ServiceInstanceInfo;
import cn.com.connext.msf.server.management.processor.ManagedSendNotificationProcessor;
import cn.com.connext.msf.server.management.repository.ManagedServiceRepository;
import cn.com.connext.msf.server.management.validator.ManagedServiceValidator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 托管服务 - 业务服务对象
 * 开发人员: 程瀚
 * 修订日期: 2019-05-30 22:36:20
 */
@Service
public class ManagedServiceService {

    private final ManagedServiceValidator managedServiceValidator;
    private final ManagedServiceRepository managedServiceRepository;
    private final ServiceInstanceManager serviceInstanceManager;
    private final RegisteredServiceManager registeredServiceManager;
    private final String serviceInstanceCountErrorMessage;
    private final String serviceMiddlewareErrorMessage;
    private final List<ManagedSendNotificationProcessor> processChain;

    /**
     * 默认实例化方法。
     *
     * @param managedServiceValidator  实体验证器
     * @param managedServiceRepository 实体持久层
     * @param serviceInstanceManager
     * @param registeredServiceManager
     * @param processChain
     */
    public ManagedServiceService(
            ManagedServiceValidator managedServiceValidator,
            ManagedServiceRepository managedServiceRepository,
            ServiceInstanceManager serviceInstanceManager,
            RegisteredServiceManager registeredServiceManager,
            List<ManagedSendNotificationProcessor> processChain) {
        this.managedServiceValidator = managedServiceValidator;
        this.managedServiceRepository = managedServiceRepository;
        this.serviceInstanceManager = serviceInstanceManager;
        this.registeredServiceManager = registeredServiceManager;
        this.processChain = processChain;
        this.serviceInstanceCountErrorMessage = "{0}服务实例数量异常：预计实例数为{1}，当前实例数为{2}；\n";
        this.serviceMiddlewareErrorMessage = "{0}服务{1}实例状态异常";
    }

    /**
     * 创建新的托管服务。
     *
     * @param managedService 所要创建的托管服务对象实例
     * @return 返回所创建的对象实例
     */
    public ManagedService create(ManagedService managedService) {
        managedServiceValidator.createValidate(managedService);
        return managedServiceRepository.create(managedService);
    }

    /**
     * 修改已有托管服务。
     *
     * @param managedService 所要修改的托管服务对象实例
     * @return 返回修改后对象实例
     */
    public ManagedService modify(ManagedService managedService) {
        managedServiceValidator.modifyValidate(managedService);
        return managedServiceRepository.modify(managedService);
    }

    /**
     * 根据主键删除托管服务。
     *
     * @param id 主键 - 唯一标识
     */
    public void delete(String id) {
        managedServiceValidator.deleteValidate(id);
        managedServiceRepository.delete(id);
    }

    /**
     * 根据主键查找托管服务。
     *
     * @param id 主键 - 唯一标识
     * @return 返回所匹配的托管服务对象实例
     */
    public ManagedService findItem(String id) {
        return managedServiceRepository.findOne(id);
    }

    /**
     * 根据查询条件，分页查询匹配的记录。
     *
     * @param queryInfo 查询条件
     * @return 返回数据分页信息
     */
    public Page<ManagedService> findPage(QueryInfo queryInfo) {
        return managedServiceRepository.findPage(queryInfo);
    }

    /**
     * 根据查询条件，查询并返回所有记录列表信息。
     *
     * @param queryInfo 查询条件
     * @return 返回数据集合信息
     */
    public List<ManagedService> findList(QueryInfo queryInfo) {
        return managedServiceRepository.findList(queryInfo);
    }

    public Page<ManagedServiceView> findViewPage(QueryInfo queryInfo) {
        Page<ManagedService> managedServicePage = findPage(queryInfo);
        List<ManagedServiceView> managedServiceViewList = new ArrayList<>();
        managedServicePage.getContent().forEach(managedService -> {
            String serviceName = managedService.getName();
            managedServiceViewList.add(ManagedServiceView.from(managedService, registeredServiceManager.findServiceInstance(serviceName).size()));
        });
        return ManagedServiceView.from(managedServicePage, managedServiceViewList);
    }

    public ManagedServiceView findViewItem(String id) {
        ManagedService managedService = findItem(id);
        String serviceName = managedService.getName();
        List<ServiceInstanceInfo> serviceInstanceInfoList = serviceInstanceManager.getServiceInstanceInfoList(registeredServiceManager.findServiceInstance(serviceName));
        return ManagedServiceView.from(managedService, serviceInstanceInfoList);
    }

    public void checkServiceHealth() {
        List<ManagedService> managedServiceList = findList(new QueryInfo());
        managedServiceList.forEach(managedService -> {
            StringBuilder errorMsg = new StringBuilder();
            String serviceName = managedService.getName();
            List<ServiceInstance> instanceList = registeredServiceManager.findServiceInstance(serviceName);
            if (managedService.getInstanceCount() != instanceList.size()) {
                errorMsg.append(MessageFormat.format(serviceInstanceCountErrorMessage, serviceName, managedService.getInstanceCount(), instanceList.size()));
            }
            List<ServiceInstanceInfo> instanceInfoList = serviceInstanceManager.getServiceInstanceInfoList(instanceList);
            instanceInfoList.forEach(info -> {
                ServiceInstanceInfo.HealthInfo healthInfo = info.getHealthInfo();

                String status = healthInfo.getStatus();
                if (!Objects.equals(status, "UP")) {
                    errorMsg.append(MessageFormat.format(serviceMiddlewareErrorMessage, serviceName, info.getServiceInstance().getHost()));
                }
            });
            if (!Validator.isEmpty(errorMsg.toString())) {
                for (ManagedSendNotificationProcessor processor : processChain) {
                    processor.process(errorMsg.toString());
                }
            }
        });
    }
}

