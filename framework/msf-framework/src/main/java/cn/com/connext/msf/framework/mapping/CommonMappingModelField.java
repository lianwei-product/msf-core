package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.dynamic.DynamicModelField;
import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import cn.com.connext.msf.framework.utils.JsonNodeLoader;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 数据模型字段
 * 开发人员: 程瀚
 * 修订日期: 2018-12-11 10:24:16
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonMappingModelField<S extends CommonMappingModelField> implements DynamicModelField<S> {

    public String name;
    public String aliasName;
    public DynamicModelFieldType type;
    public boolean arrayType;
    public String defaultValue;
    public boolean allowNull;
    public List<S> fields;

    public CommonMappingModelField() {

    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull, List<T> fieldList, Consumer<T> consumer) {
        T field = supplier.get();
        field.setName(name);
        field.setAliasName(aliasName);
        field.setType(type);
        field.setArrayType(arrayType);
        field.setDefaultValue(defaultValue);
        field.setAllowNull(allowNull);

        if (!CollectionUtils.isEmpty(fieldList)) {
            field.getFields().addAll(fieldList);
        }

        if (null != consumer) {
            consumer.accept(field);
        }

        return field;
    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull, List<T> fieldList) {
        return from(supplier, name, aliasName, type, arrayType, defaultValue, allowNull, fieldList, null);
    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull) {
        return from(supplier, name, aliasName, type, arrayType, defaultValue, allowNull, null, null);
    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue) {
        return from(supplier, name, aliasName, type, arrayType, defaultValue, true);
    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType) {
        return from(supplier, name, aliasName, type, arrayType, null);
    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, Consumer<T> consumer) {
        return from(supplier, name, aliasName, type, false, null, true, null, consumer);
    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type) {
        return from(supplier, name, aliasName, type, false);
    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName) {
        return from(supplier, name, aliasName, DynamicModelFieldType.KEYWORD);
    }

    public static <T extends CommonMappingModelField> T from(Supplier<T> supplier, String name, String aliasName, Consumer<T> consumer) {
        return from(supplier, name, aliasName, DynamicModelFieldType.KEYWORD, false, null, true, null, consumer);
    }

    @Override
    public boolean isAllowNull() {
        return allowNull;
    }

    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public DynamicModelFieldType getType() {
        return type;
    }

    public void setType(DynamicModelFieldType type) {
        this.type = type;
    }

    @Override
    public boolean isArrayType() {
        return arrayType;
    }

    public void setArrayType(boolean arrayType) {
        this.arrayType = arrayType;
    }

    @Override
    public List<S> getFields() {
        if (CollectionUtils.isEmpty(fields)) {
            fields = Lists.newArrayList();
        }
        return fields;
    }

    public JsonNode loadDefaultJsonNode() {
        if (StringUtils.isEmpty(this.getDefaultValue()))
            return null;
        JsonNode defaultNode = JsonNodeLoader.loadJsonNodeFromString(this.getType(), this.getDefaultValue());
        if (this.isArrayType()) {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            arrayNode.add(defaultNode);
            return arrayNode;
        } else {
            return defaultNode;
        }
    }

    public void setFields(List<S> fields) {
        this.fields = fields;
    }

}
