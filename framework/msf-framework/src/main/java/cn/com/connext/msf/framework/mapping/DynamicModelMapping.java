package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.utils.JSON;

/**
 * 数据模型字段映射关系接口
 * 开发人员: 许有兵
 * 修订日期: 2020-09-09 13:42:16
 */
public interface DynamicModelMapping {

    /*
     * 来源字段名称
     */
    String getSourceFieldName();

    /*
     * 目标字段名称
     */
    String getDestFieldName();

    /*
     * 映射方式
     */
    DynamicFieldMappingType getMappingType();

    /*
     * 映射内容设置
     */
    Object getMappingSetting();

    /**
     * 转换为目标类型
     */
    default <T> T convertMappingSetting(Class<T> tClass) {
        Object mappingSetting = this.getMappingSetting();
        if (mappingSetting != null && tClass.isAssignableFrom(mappingSetting.getClass())) {
            return (T) mappingSetting;
        }
        // TODO: 2020/11/6 0006 若tClass没有默认构造函数，该方法无法parse
        return JSON.parseObject(JSON.toJsonString(mappingSetting), tClass);
    }
}
