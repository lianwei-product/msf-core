package cn.com.connext.msf.framework.utils;

import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import cn.com.connext.msf.framework.mapping.DynamicModelMapping;
import cn.com.connext.msf.framework.mapping.model.MultiSourceFieldModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class JsonNodeLoader {

    public static JsonNode loadJsonNodeFromObject(DynamicModelFieldType destFieldType, Object value) {
        return loadJsonNodeFromString(destFieldType, String.valueOf(value));
    }

    public static JsonNode loadJsonNodeFromString(DynamicModelFieldType destFieldType, String value) {
        if (!StringUtils.isEmpty(value)) {
            JsonNode jsonNode = null;
            switch (destFieldType) {
                case SHORT:
                    jsonNode = new ShortNode(Short.parseShort(value));
                    break;

                case INTEGER:
                    jsonNode = new IntNode(Integer.parseInt(value));
                    break;

                case LONG:
                    jsonNode = new LongNode(Long.parseLong(value));
                    break;

                case DOUBLE:
                    jsonNode = new DoubleNode(Double.parseDouble(value));
                    break;

                case FLOAT:
                    jsonNode = new FloatNode(Float.parseFloat(value));
                    break;

                case KEYWORD:
                case TEXT:
                case DATE:
                    jsonNode = new TextNode(value);
                    break;

                case BOOLEAN:
                    jsonNode = BooleanNode.valueOf(Boolean.parseBoolean(value));
                    break;
            }
            return jsonNode;
        }
        return null;
    }

    public static JsonNode loadJsonNodeFromJsonNode(DynamicModelFieldType destFieldType, JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return jsonNode;
        }

        switch (destFieldType) {
            case SHORT:
                if (!jsonNode.isNumber() || !Validator.isShort(jsonNode.asText(null))) {
                    return new ShortNode(Short.parseShort(jsonNode.asText(null)));
                }
                break;

            case INTEGER:
                if (!jsonNode.isNumber() || !Validator.isInteger(jsonNode.asText(null))) {
                    return new IntNode(Integer.parseInt(jsonNode.asText(null)));
                }
                break;

            case LONG:
                if (!jsonNode.isNumber() || !Validator.isLong(jsonNode.asText(null))) {
                    return new LongNode(Long.parseLong(jsonNode.asText(null)));
                }
                break;

            case DOUBLE:
                if (!jsonNode.isNumber() || !Validator.isDouble(jsonNode.asText(null))) {
                    return new DoubleNode(Double.parseDouble(jsonNode.asText(null)));
                }
                break;

            case FLOAT:
                if (!jsonNode.isNumber() || !Validator.isFloat(jsonNode.asText(null))) {
                    return new FloatNode(Float.parseFloat(jsonNode.asText(null)));
                }
                break;

            case KEYWORD:
            case TEXT:
            case DATE:
                if (!jsonNode.isTextual()) {
                    return new TextNode(jsonNode.asText(null));
                }
                break;

            case BOOLEAN:
                if (!jsonNode.isBoolean()) {
                    return BooleanNode.valueOf(Boolean.parseBoolean(jsonNode.asText(null)));
                }
                break;
        }

        return jsonNode;
    }

    public static JsonNode loadFixedValueJsonNode(boolean isArrayType, DynamicModelFieldType destFieldType, Object mappingSetting) {
        if (!org.springframework.util.StringUtils.isEmpty(mappingSetting)) {
            JsonNode defaultNode = JsonNodeLoader.loadJsonNodeFromObject(destFieldType, mappingSetting);
            if (isArrayType) {
                ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
                arrayNode.add(defaultNode);
                return arrayNode;
            } else {
                return defaultNode;
            }
        }
        return null;
    }

    public static JsonNode loadMultiSourceFieldJsonNode(boolean isArrayType, JsonNode node, DynamicModelMapping dataModelMapping) {
        MultiSourceFieldModel multiSourceFieldModel = dataModelMapping.convertMappingSetting(MultiSourceFieldModel.class);
        if (multiSourceFieldModel == null || multiSourceFieldModel.getSourceFieldNames() == null
                || multiSourceFieldModel.getSourceFieldNames().size() == 0)
            return null;

        JsonNode jsonNode = null;
        if (node.isArray()) {
            if (node.size() > 0) {
                jsonNode = node.get(0);
            }
        } else {
            jsonNode = node;
        }

        if (jsonNode == null)
            return null;

        List<String> sourceFieldValueList = Lists.newArrayList();
        for (String fieldName : multiSourceFieldModel.getSourceFieldNames()) {
            JsonNode fieldNode = jsonNode.get(fieldName);

            if (fieldNode != null && !org.springframework.util.StringUtils.isEmpty(fieldNode.asText(null)))
                sourceFieldValueList.add(fieldNode.asText());
        }

        String sourceFieldValue = StringUtils.join(sourceFieldValueList.toArray(), multiSourceFieldModel.getSeparator());
        JsonNode textNode = new TextNode(sourceFieldValue);

        if (isArrayType) {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            arrayNode.add(textNode);
            return arrayNode;
        } else {
            return textNode;
        }
    }
}
