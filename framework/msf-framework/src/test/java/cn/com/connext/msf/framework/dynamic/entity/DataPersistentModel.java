package cn.com.connext.msf.framework.dynamic.entity;

import cn.com.connext.msf.framework.annotation.QueryVerb;
import cn.com.connext.msf.framework.dynamic.builder.DataModelField;
import cn.com.connext.msf.framework.utils.Base58UUID;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 数据持久化模型 - 实体定义
 * 开发人员: 程瀚
 * 修订日期: 2018-12-11 10:24:16
 */
@Document(collection = "data_persistent_model")
public class DataPersistentModel extends CdpDataModel<DataModelField> {

//    @Id
//    @ApiModelProperty(value = "唯一标识", readOnly = true, example = "auto")
//    private String id;

    @Indexed
    @ApiModelProperty(value = "租户标识", required = true)
    @QueryVerb(operator = "eq")
    private String tenantId;

    @Indexed
    @ApiModelProperty(value = "所属集合", required = true)
    @QueryVerb(operator = "eq")
    private String collectionId;

//    @ApiModelProperty(value = "模型名称", required = true)
//    private String name;
//
//    @ApiModelProperty(value = "显示名称", required = true)
//    private String aliasName;
//
//    @ApiModelProperty(value = "字段集合", required = true)
//    private List<DataModelField> fields;

    @ApiModelProperty(value = "摘要说明", required = false)
    private String description;

    /**
     * 默认实例化方法
     */
    public DataPersistentModel() {
        id = Base58UUID.newBase58UUID();
        fields = Lists.newArrayList();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAliasName() {
        return aliasName;
    }

    @Override
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public List<DataModelField> getFields() {
        return fields;
    }

    @Override
    public void setFields(List<DataModelField> fields) {
        this.fields = fields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DataPersistentModel{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", collectionId='" + collectionId + '\'' +
                ", name='" + name + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", fields=" + fields +
                ", description='" + description + '\'' +
                ", fields=" + fields +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", aliasName='" + aliasName + '\'' +
                '}';
    }
}