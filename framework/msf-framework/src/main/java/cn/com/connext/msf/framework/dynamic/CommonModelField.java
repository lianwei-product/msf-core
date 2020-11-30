package cn.com.connext.msf.framework.dynamic;

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

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonModelField<S extends CommonModelField> implements DynamicModelField<S> {

    public String name;
    public String aliasName;
    public DynamicModelFieldType type;
    public boolean arrayType;
    public String defaultValue;
    public boolean allowNull;
    public List<S> fields;

    public CommonModelField() {

    }

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull, List<T> fieldList, Consumer<T> consumer) {
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

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull, List<T> fieldList) {
        return from(supplier, name, aliasName, type, arrayType, defaultValue, allowNull, fieldList, null);
    }

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull) {
        return from(supplier, name, aliasName, type, arrayType, defaultValue, allowNull, null, null);
    }

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue) {
        return from(supplier, name, aliasName, type, arrayType, defaultValue, true);
    }

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType) {
        return from(supplier, name, aliasName, type, arrayType, null);
    }

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type, Consumer<T> consumer) {
        return from(supplier, name, aliasName, type, false, null, true, null, consumer);
    }

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName, DynamicModelFieldType type) {
        return from(supplier, name, aliasName, type, false);
    }

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName) {
        return from(supplier, name, aliasName, DynamicModelFieldType.KEYWORD);
    }

    public static <T extends CommonModelField> T from(Supplier<T> supplier, String name, String aliasName, Consumer<T> consumer) {
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

    @Override
    public String toString() {
        return "CommonModelField{" +
                "name='" + name + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", type=" + type +
                ", arrayType=" + arrayType +
                ", defaultValue='" + defaultValue + '\'' +
                ", allowNull=" + allowNull +
                ", fields=" + fields +
                '}';
    }

}
