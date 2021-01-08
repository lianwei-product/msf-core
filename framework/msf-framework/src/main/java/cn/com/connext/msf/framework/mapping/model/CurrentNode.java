package cn.com.connext.msf.framework.mapping.model;

import cn.com.connext.msf.framework.dynamic.DynamicModelField;
import cn.com.connext.msf.framework.mapping.DynamicFieldMappingType;
import cn.com.connext.msf.framework.mapping.DynamicModelMapping;
import cn.com.connext.msf.framework.utils.JsonNodeLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.util.Map;

public class CurrentNode {
    private DynamicModelField dataModelField;
    private DynamicModelMapping dataModelMapping;
    private JsonNode node = null;
    private String name = null;

    public static CurrentNode from(DynamicModelField dataModelField, DynamicModelMapping dataModelMapping, JsonNode node, String name) {
        CurrentNode currentNode = new CurrentNode();
        currentNode.dataModelField = dataModelField;
        currentNode.dataModelMapping = dataModelMapping;
        currentNode.node = node;
        currentNode.name = name;
        return currentNode;
    }

    public JsonNode getValue(boolean isArray) {
        return isArray ? getArrayValue() : getObjectValue();
    }

    JsonNode getArrayValue() {
        if (dataModelMapping.getMappingType().equals(DynamicFieldMappingType.FIXED_VALUE)) {
            return JsonNodeLoader.loadFixedValueJsonNode(true, dataModelField.getType(), dataModelMapping.getMappingSetting());
        }

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        if (node.isArray()) {
            for (JsonNode item : node) {
                JsonNode jsonValue = item.get(name);
                if (jsonValue != null) {
                    arrayNode.add(jsonValue);
                }
            }
        } else {
            JsonNode jsonValue = node.get(name);
            if (jsonValue == null) {
                return arrayNode;
            }

            if (jsonValue.isArray()) {
                for (JsonNode item : jsonValue) {
                    arrayNode.add(item);
                }
            } else {
                arrayNode.add(jsonValue);
            }
        }

        ArrayNode finalArrayNode = JsonNodeFactory.instance.arrayNode();
        for (JsonNode jsonNode : arrayNode) {
            finalArrayNode.add(convertByMappingTypes(jsonNode));
        }
        return finalArrayNode;
    }

    JsonNode getObjectValue() {
        switch (dataModelMapping.getMappingType()) {
            case FIXED_VALUE: {
                return JsonNodeLoader.loadFixedValueJsonNode(false, dataModelField.getType(), dataModelMapping.getMappingSetting());
            }

            case FROM_MULTI_SOURCE_FIELD: {
                return JsonNodeLoader.loadMultiSourceFieldJsonNode(false, node, dataModelMapping);
            }
            case CONDITION_EXP: {
                // FIXME: 2021/1/8
                AviatorModel mappingSetting = dataModelMapping.convertMappingSetting(AviatorModel.class);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> env = mapper.convertValue(node, Map.class);
                mappingSetting.setEnv(env);
//                mappingSetting.getEnv().put("src", ObjectNodeUtil.getString(jsonNode, ""));
                return JsonNodeLoader.loadJsonNodeFromObject(dataModelField.getType(), mappingSetting.excute());
            }

            default: {
                JsonNode jsonValue = null;
                if (node.isArray()) {
                    if (node.size() > 0) {
                        jsonValue = node.get(0);
                        if (jsonValue == null) {
                            return null;
                        } else {
                            jsonValue = jsonValue.get(name);
                        }
                    }
                } else {
                    jsonValue = node.get(name);
                    if (jsonValue == null) {
                        return null;
                    }
                }

                jsonValue = (jsonValue != null && jsonValue.isArray()) ? jsonValue.get(0) : jsonValue;
                return convertByMappingTypes(jsonValue);
            }
        }
    }

    private JsonNode convertByMappingTypes(JsonNode jsonNode) {
        switch (dataModelMapping.getMappingType()) {
            case FROM_DICTIONARY: {
                DictModel mappingSetting = dataModelMapping.convertMappingSetting(DictModel.class);
                return JsonNodeLoader.loadJsonNodeFromString(dataModelField.getType(), mappingSetting.getDict().get(jsonNode.asText(null)));
            }

            default: {
                return JsonNodeLoader.loadJsonNodeFromJsonNode(dataModelField.getType(), jsonNode);
            }
        }
    }
}
