package cn.com.connext.msf.framework.mapping;

/**
 * 字段映射类型
 * 开发人员: 许有兵
 * 修订日期: 2020-09-09 13:42:16
 */
public enum DynamicFieldMappingType {

    FROM_SOURCE_FIELD("from_sourceField"),
    FROM_MULTI_SOURCE_FIELD("from_multi_sourceField"),
    FROM_DICTIONARY("from_dictionary"),
    FIXED_VALUE("fixed_value"),
    CONDITION_EXP("condition_exp"),
    NO_MAPPING("no_mapping"),
    ;

    String dynamicFieldMappingType;

    DynamicFieldMappingType(String dynamicFieldMappingType) {
        this.dynamicFieldMappingType = dynamicFieldMappingType;
    }

    public String getDynamicFieldMappingType() {
        return dynamicFieldMappingType;
    }
}
