package cn.com.connext.msf.framework.dynamic;

import cn.com.connext.msf.framework.utils.JSON;

import java.util.List;

public interface DynamicModel<S extends DynamicModelField> {

    /**
     * 唯一标识
     */
    String getId();

    /**
     * 模型名称
     */
    String getName();

    /**
     * 显示名称
     */
    String getAliasName();

    /**
     * 字段信息
     */
    List<S> getFields();

    /**
     * 转换为目标类型
     */
    default <T> T convert(Class<T> tClass) {
        return JSON.parseObject(JSON.toJsonString(this), tClass);
    }
}
