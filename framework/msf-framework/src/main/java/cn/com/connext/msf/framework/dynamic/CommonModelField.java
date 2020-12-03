package cn.com.connext.msf.framework.dynamic;

import cn.com.connext.msf.framework.utils.JsonNodeLoader;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.util.StringUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonModelField implements DynamicModelField {

    private String name;
    private String aliasName;
    private DynamicModelFieldType type;
    private boolean arrayType;
    private List<CommonModelField> fields;
    public String defaultValue;
    public boolean allowNull;

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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    @Override
    public boolean isAllowNull() {
        return allowNull;
    }

    @Override
    public JsonNode loadDefaultJsonNode() {
        if (StringUtils.isEmpty(this.getDefaultValue())) {
            return null;
        } else {
            JsonNode defaultNode = JsonNodeLoader.loadJsonNodeFromString(this.getType(), this.getDefaultValue());
            if (this.isArrayType()) {
                ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
                arrayNode.add(defaultNode);
                return arrayNode;
            } else {
                return defaultNode;
            }
        }
    }

}
