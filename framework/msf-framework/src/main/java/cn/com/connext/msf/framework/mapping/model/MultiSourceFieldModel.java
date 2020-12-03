package cn.com.connext.msf.framework.mapping.model;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 数据模型字段映射关系-多来源字段模型
 * 开发人员: 许有兵
 * 修订日期: 2019-02-27 15:13:32
 */

public class MultiSourceFieldModel {
    @ApiModelProperty(value = "分隔符", example = "-", required = true)
    private String separator;

    @ApiModelProperty(value = "来源字段名称列表", example = "[\"province\", \"city\", \"district\"]", required = true)
    private List<String> sourceFieldNames;


    public MultiSourceFieldModel() {
        sourceFieldNames = Lists.newArrayList();
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public List<String> getSourceFieldNames() {
        return sourceFieldNames;
    }

    public void setSourceFieldNames(List<String> sourceFieldNames) {
        this.sourceFieldNames = sourceFieldNames;
    }
}
