package cn.com.connext.msf.server.management.entity;

import cn.com.connext.msf.framework.utils.Base58UUID;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 托管服务 - 实体定义
 * 开发人员: 程瀚
 * 修订日期: 2019-05-30 22:36:20
 */
@Document(collection = "management_managed_service")
public class ManagedService {

    @Id
    @ApiModelProperty(value = "唯一标识", readOnly = true, example = "auto")
    private String id;

    @Indexed(unique = true)
    @ApiModelProperty(value = "服务名称", required = true)
    private String name;

    @ApiModelProperty(value = "标准实例数据", required = true)
    private int instanceCount;

    @ApiModelProperty(value = "服务描述", required = true)
    private String description;

    /**
     * 默认实例化方法
     */
    public ManagedService() {
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

