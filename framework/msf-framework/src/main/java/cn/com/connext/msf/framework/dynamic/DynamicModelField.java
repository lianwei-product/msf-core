package cn.com.connext.msf.framework.dynamic;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface DynamicModelField<S extends DynamicModelField> {

    /**
     * 字段名称
     */
    String getName();

    /**
     * 显示名称
     */
    String getAliasName();

    /**
     * 字段类型
     */
    DynamicModelFieldType getType();

    /**
     * 是否数组类型
     */
    boolean isArrayType();

    /**
     * 字段信息
     */
    List<S> getFields();

    /**
     * 获取默认node
     */
    default JsonNode loadDefaultJsonNode() {
        return null;
    }

    /**
     * 字段是否允许为空，默认true可为空，
     * {@link javax.validation.constraints.NotNull}
     */
    default boolean isAllowNull() {
        return true;
    }
}
