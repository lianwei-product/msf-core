package cn.com.connext.msf.framework.mapping.entity;

import cn.com.connext.msf.framework.mapping.CommonMappingModelField;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据模型字段
 * 开发人员: 程瀚
 * 修订日期: 2018-12-11 10:24:16
 */
public class CdpDataModelField extends CommonMappingModelField<CdpDataModelField> {

    @ApiModelProperty(value = "数据类型数据采用合并模式")
    private boolean arrayDataMergeMode;

    @ApiModelProperty(value = "仅用于存储，不索引查询")
    private boolean onlyStorage;

    @ApiModelProperty(value = "字段扩展信息")
    private String extendInfo;

    // 不是所有的字段客户都需要在标签计算中显示，如果字段过多用户会难选择和使用
    @ApiModelProperty(value = "是否用于标签计算", required = false)
    private Boolean useForTag;

    @ApiModelProperty(value = "关联字典标识")
    private String dictionaryId;

    public CdpDataModelField() {
    }

    public boolean isArrayDataMergeMode() {
        return arrayDataMergeMode;
    }

    public void setArrayDataMergeMode(boolean arrayDataMergeMode) {
        this.arrayDataMergeMode = arrayDataMergeMode;
    }

    public boolean isOnlyStorage() {
        return onlyStorage;
    }

    public void setOnlyStorage(boolean onlyStorage) {
        this.onlyStorage = onlyStorage;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public Boolean getUseForTag() {
        return useForTag;
    }

    public void setUseForTag(Boolean useForTag) {
        this.useForTag = useForTag;
    }

    public String getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(String dictionaryId) {
        this.dictionaryId = dictionaryId;
    }
}
