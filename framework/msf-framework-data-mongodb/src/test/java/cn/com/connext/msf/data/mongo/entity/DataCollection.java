package cn.com.connext.msf.data.mongo.entity;

import cn.com.connext.msf.framework.annotation.QueryVerb;
import cn.com.connext.msf.framework.utils.Base58UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 数据集合 - 实体定义
 * 开发人员: 程瀚
 * 修订日期: 2018-12-11 10:24:16
 */
@Document(collection = "data_collection")
public class DataCollection {

    @Id
    private String id;

    @Indexed
    @QueryVerb(operator = "eq")
    private String tenantId;

    @Indexed
    @QueryVerb(operator = "eq")
    private String name;

    private String aliasName;

    private String description;

    /**
     * 默认实例化方法
     */
    public DataCollection() {
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
     * 获取租户标识。
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * 设置租户标识。
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取集合名称。
     */
    public String getName() {
        return name;
    }

    /**
     * 设置集合名称。
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取显示名称。
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * 设置显示名称。
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * 获取摘要说明。
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置摘要说明。
     */
    public void setDescription(String description) {
        this.description = description;
    }

    //endregion
}

