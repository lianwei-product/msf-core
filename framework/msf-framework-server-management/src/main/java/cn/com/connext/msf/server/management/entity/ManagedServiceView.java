package cn.com.connext.msf.server.management.entity;

import cn.com.connext.msf.framework.utils.Base58UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;


public class ManagedServiceView {

    private String id;

    private String name;

    private int instanceCount;

    //当前实例数量
    private int currentInstanceCount;

    private String description;

    private List<ServiceInstanceInfo> serviceInstanceInfoList;

    public static Page<ManagedServiceView> from(Page<ManagedService> page,
                                                List<ManagedServiceView> managedServiceViewList) {
        return new PageImpl<>(
                managedServiceViewList,
                page.getPageable(),
                page.getTotalElements()
        );
    }

    public static ManagedServiceView from(ManagedService managedService, List<ServiceInstanceInfo> serviceInstanceInfoList) {
        ManagedServiceView managedServiceView = new ManagedServiceView();
        managedServiceView.id = managedService.getId();
        managedServiceView.name = managedService.getName();
        managedServiceView.instanceCount = managedService.getInstanceCount();
        managedServiceView.currentInstanceCount = serviceInstanceInfoList.size();
        managedServiceView.description = managedService.getDescription();
        managedServiceView.serviceInstanceInfoList = serviceInstanceInfoList;
        return managedServiceView;
    }

    public static ManagedServiceView from(ManagedService managedService, int currentInstanceCount) {
        ManagedServiceView managedServiceView = new ManagedServiceView();
        managedServiceView.id = managedService.getId();
        managedServiceView.name = managedService.getName();
        managedServiceView.instanceCount = managedService.getInstanceCount();
        managedServiceView.currentInstanceCount = currentInstanceCount;
        managedServiceView.description = managedService.getDescription();
        return managedServiceView;
    }


    public List<ServiceInstanceInfo> getServiceInstanceInfoList() {
        return serviceInstanceInfoList;
    }

    public void setServiceInstanceInfoList(List<ServiceInstanceInfo> serviceInstanceInfoList) {
        this.serviceInstanceInfoList = serviceInstanceInfoList;
    }


    public int getCurrentInstanceCount() {
        return currentInstanceCount;
    }

    public void setCurrentInstanceCount(int currentInstanceCount) {
        this.currentInstanceCount = currentInstanceCount;
    }

    /**
     * 默认实例化方法
     */
    public ManagedServiceView() {
        id = Base58UUID.newBase58UUID();
    }

    //region getter & setter

    /**
     * 获取唯一标识。
     */
    public String getId() {
        return id;
    }

    /**
     * 设置唯一标识。
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取服务名称。
     */
    public String getName() {
        return name;
    }

    /**
     * 设置服务名称。
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取标准实例数据。
     */
    public int getInstanceCount() {
        return instanceCount;
    }

    /**
     * 设置标准实例数据。
     */
    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

    /**
     * 获取服务描述。
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置服务描述。
     */
    public void setDescription(String description) {
        this.description = description;
    }

    //endregion
}

