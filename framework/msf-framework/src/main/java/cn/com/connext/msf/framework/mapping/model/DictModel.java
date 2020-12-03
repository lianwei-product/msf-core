package cn.com.connext.msf.framework.mapping.model;

import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;

/**
 * @Author: ming.wang
 * @Date: 2020/10/26 0026 14:10
 * @Description:
 */
public class DictModel {
    @ApiModelProperty(value = "字典信息", required = true)
    private HashMap<String, String> dict;

    /**
     * 默认实例化方法
     */
    public DictModel() {
        dict = Maps.newHashMap();
    }

    public HashMap<String, String> getDict() {
        return dict;
    }

    public void setDict(HashMap<String, String> dict) {
        this.dict = dict;
    }

    public void addDictItem(String key, String value) {
        dict.put(key, value);
    }
}

