package cn.com.connext.msf.framework.dynamic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonModel extends CommonModelInfo implements DynamicModel<CommonModelField> {

    private List<CommonModelField> fields;

    public CommonModel() {
        fields = Lists.newArrayList();
    }

    public List<CommonModelField> getFields() {
        return fields;
    }

    public void setFields(List<CommonModelField> fields) {
        this.fields = fields;
    }
}
