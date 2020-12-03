package cn.com.connext.msf.framework.mapping;

/**
 * 数据模型字段映射关系
 * 开发人员: 许有兵
 * 修订日期: 2020-09-09 13:42:16
 */
public class CommonModelMapping implements DynamicModelMapping {
    /*
     * 来源字段名称
     */
    public String sourceFieldName;

    /*
     * 目标字段名称
     */
    public String destFieldName;

    /*
     * 映射方式
     */
    public DynamicFieldMappingType mappingType;

    /**
     * 映射内容设置
     */
    public Object mappingSetting;

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public void setSourceFieldName(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    public String getDestFieldName() {
        return destFieldName;
    }

    public void setDestFieldName(String destFieldName) {
        this.destFieldName = destFieldName;
    }

    public DynamicFieldMappingType getMappingType() {
        return mappingType;
    }

    public void setMappingType(DynamicFieldMappingType mappingType) {
        this.mappingType = mappingType;
    }

    public Object getMappingSetting() {
        return mappingSetting;
    }

    public void setMappingSetting(Object mappingSetting) {
        this.mappingSetting = mappingSetting;
    }
}
