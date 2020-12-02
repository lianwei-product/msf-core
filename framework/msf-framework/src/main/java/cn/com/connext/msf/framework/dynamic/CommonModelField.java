package cn.com.connext.msf.framework.dynamic;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonModelField implements DynamicModelField {

    private String name;
    private String aliasName;
    private DynamicModelFieldType type;
    private boolean arrayType;
    private List<CommonModelField> fields;

    public CommonModelField() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public DynamicModelFieldType getType() {
        return type;
    }

    public void setType(DynamicModelFieldType type) {
        this.type = type;
    }

    public boolean isArrayType() {
        return arrayType;
    }

    public void setArrayType(boolean arrayType) {
        this.arrayType = arrayType;
    }

    public List<CommonModelField> getFields() {
        return fields;
    }

    public void setFields(List<CommonModelField> fields) {
        this.fields = fields;
    }
}
