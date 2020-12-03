package cn.com.connext.msf.framework.mapping;

import java.util.function.Supplier;

/**
 * mapping构造器
 * 开发人员: 许有兵
 * 修订日期: 2020-09-09 13:42:16
 */
public class CommonModelMappingBuilder<T extends CommonModelMapping> {

    public T build(Supplier<T> supplier, DynamicFieldMappingType mappingType, String destFieldName, String sourceFieldName, Object mappingSetting) {
        T t = supplier.get();
        t.setSourceFieldName(sourceFieldName);
        t.setDestFieldName(destFieldName);
        t.setMappingType(mappingType);
        t.setMappingSetting(mappingSetting);
        return t;
    }


    public T buildFromSourceField(Supplier<T> supplier, String destFieldName, String sourceFieldName) {
        return build(supplier, DynamicFieldMappingType.FROM_SOURCE_FIELD, destFieldName, sourceFieldName, "");
    }

    public T buildFromSourceField(Supplier<T> supplier, String name) {
        return buildFromSourceField(supplier, name, name);
    }

    public T buildConditionExp(Supplier<T> supplier, String destFieldName, String sourceFieldName, Object mappingSetting) {
        return build(supplier, DynamicFieldMappingType.CONDITION_EXP, destFieldName, sourceFieldName, mappingSetting);
    }

    public T buildFixedValue(Supplier<T> supplier, String destFieldName, Object mappingSetting) {
        return build(supplier, DynamicFieldMappingType.FIXED_VALUE, destFieldName, "", mappingSetting);
    }

    public T buildFromDictionary(Supplier<T> supplier, String destFieldName, String sourceFieldName, Object mappingSetting) {
        return build(supplier, DynamicFieldMappingType.FROM_DICTIONARY, destFieldName, sourceFieldName, mappingSetting);
    }

    public T buildFromMultiSourceField(Supplier<T> supplier, String destFieldName, Object mappingSetting) {
        return build(supplier, DynamicFieldMappingType.FROM_MULTI_SOURCE_FIELD, destFieldName, "", mappingSetting);
    }

    public T buildNoMapping(Supplier<T> supplier, String destFieldName, String sourceFieldName, Object mappingSetting) {
        return build(supplier, DynamicFieldMappingType.NO_MAPPING, destFieldName, sourceFieldName, mappingSetting);
    }


    public static CommonModelMapping build(DynamicFieldMappingType mappingType, String destFieldName, String sourceFieldName, Object mappingSetting) {
        Supplier<CommonModelMapping> supplier = () -> new CommonModelMapping();
        CommonModelMapping t = supplier.get();
        t.setSourceFieldName(sourceFieldName);
        t.setDestFieldName(destFieldName);
        t.setMappingType(mappingType);
        t.setMappingSetting(mappingSetting);
        return t;
    }

    public static CommonModelMapping buildFromSourceField(String destFieldName, String sourceFieldName) {
        return build(DynamicFieldMappingType.FROM_SOURCE_FIELD, destFieldName, sourceFieldName, "");
    }

    public static CommonModelMapping buildFromSourceField(String name) {
        return buildFromSourceField(name, name);
    }

    public static CommonModelMapping buildConditionExp(String destFieldName, String sourceFieldName, Object mappingSetting) {
        return build(DynamicFieldMappingType.CONDITION_EXP, destFieldName, sourceFieldName, mappingSetting);
    }

    public static CommonModelMapping buildFixedValue(String destFieldName, Object mappingSetting) {
        return build(DynamicFieldMappingType.FIXED_VALUE, destFieldName, "", mappingSetting);
    }

    public static CommonModelMapping buildFromDictionary(String destFieldName, String sourceFieldName, Object mappingSetting) {
        return build(DynamicFieldMappingType.FROM_DICTIONARY, destFieldName, sourceFieldName, mappingSetting);
    }

    public static CommonModelMapping buildFromMultiSourceField(String destFieldName, Object mappingSetting) {
        return build(DynamicFieldMappingType.FROM_MULTI_SOURCE_FIELD, destFieldName, "", mappingSetting);
    }

    public static CommonModelMapping buildNoMapping(String destFieldName, String sourceFieldName, Object mappingSetting) {
        return build(DynamicFieldMappingType.NO_MAPPING, destFieldName, sourceFieldName, mappingSetting);
    }

}
