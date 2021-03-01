package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.dynamic.*;
import cn.com.connext.msf.framework.mapping.model.CurrentArray;
import cn.com.connext.msf.framework.mapping.model.CurrentNode;
import cn.com.connext.msf.framework.utils.JsonNodeTypeValidator;
import cn.com.connext.msf.framework.utils.ObjectNodeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class DataMapper {
    private static Logger logger = LoggerFactory.getLogger(DataMapper.class);
    private static CommonModelMappingBuilder<CommonModelMapping> builder = new CommonModelMappingBuilder();
    private static Supplier<CommonModelMapping> supplier = () -> new CommonModelMapping();

    public static ObjectNode mapping(ObjectNode sourceNode, DynamicModel targetModel, List<? extends DynamicModelMapping> mappings) {
        return mapping(sourceNode, targetModel, mappings, true);
    }

    public static ObjectNode mapping(ObjectNode sourceNode, DynamicModel targetModel, List<? extends DynamicModelMapping> mappings, boolean doValid) {
        CommonModel commonModel = (CommonModel) targetModel.convert(CommonModel.class);
        List<CommonModelField> fields = commonModel.getFields();
        Map<String, DynamicModelMapping> fieldMap = initFieldMap(mappings, fields);
        return mapping(fieldMap, sourceNode, JsonNodeFactory.instance.objectNode(), fields, doValid);
    }

    public static <T extends DynamicModelMapping, S> S simpleMapping(ObjectNode sourceNode, Class<S> targetModel, T mapping, boolean doValid) {
        Map<String, DynamicModelMapping> fieldMap = initFieldMap(Lists.newArrayList(mapping));
        List<CommonModelField> targetModelFieldList = Lists.newArrayList();

        CommonModelField field = new CommonModelField();
        field.setName("result");
        field.setType(DynamicModelFieldType.fromClass(targetModel));
        targetModelFieldList.add(field);

        ObjectNode result = mapping(fieldMap, sourceNode, JsonNodeFactory.instance.objectNode(), targetModelFieldList, doValid);
        return ObjectNodeUtil.getObject(result, "result", targetModel);
    }

    private static Map<String, DynamicModelMapping> initFieldMap(List<? extends DynamicModelMapping> mappings) {
        Map<String, DynamicModelMapping> fieldMap = Maps.newHashMap();
        mappings.forEach(mapping -> {
            if (!Objects.equals(DynamicFieldMappingType.NO_MAPPING, mapping.getMappingType())) {
                fieldMap.put(mapping.getDestFieldName(), mapping);
            }
        });
        return fieldMap;
    }

    private static Map<String, DynamicModelMapping> initFieldMap(List<? extends DynamicModelMapping> mappings, List<CommonModelField> fields) {
        Map<String, DynamicModelMapping> fieldMap = Maps.newHashMap();
        mappings.forEach(mapping -> {
            if (!Objects.equals(DynamicFieldMappingType.NO_MAPPING, mapping.getMappingType())) {
                Optional<CommonModelField> first = fields.stream().filter(x -> x.getName().equals(mapping.getDestFieldName()) && x.getType() == DynamicModelFieldType.NESTED).findFirst();
                if (mapping.getMappingType() == DynamicFieldMappingType.FROM_SOURCE_FIELD && first.isPresent()
                        ) {
                    CommonModelField field = first.get();
                    initFieldMapItem("", field, fieldMap);
                } else {
                    fieldMap.put(mapping.getDestFieldName(), mapping);
                }
            }
        });
        return fieldMap;
    }

    private static void initFieldMapItem(String prefix, CommonModelField field, Map<String, DynamicModelMapping> fieldMap) {
        if (StringUtils.isBlank(prefix)) {
            String fieldName = field.getName();
            CommonModelMapping mapping = builder.buildFromSourceField(supplier, fieldName);
            fieldMap.put(fieldName, mapping);
        } else {

        }

        // TODO: 2021/3/1
        if (!CollectionUtils.isEmpty(field.getFields())) {
            field.getFields().forEach(x ->
                    initFieldMapItem(prefix, x, fieldMap)
            );
        }
    }

    private static ObjectNode mapping(Map<String, DynamicModelMapping> fieldMap, ObjectNode sourceNode, ObjectNode destNode, List<CommonModelField> targetModelFieldList, boolean doValid) {
        mappingObject(fieldMap, sourceNode, destNode, targetModelFieldList, "", "", doValid);
        return destNode;
    }

    private static ObjectNode mappingObject(Map<String, DynamicModelMapping> fieldMap, ObjectNode sourceNode, ObjectNode destNode, List<CommonModelField> destFieldList, String destPrefix, String sourcePrefix, boolean doValid) {
        destFieldList.forEach(field -> {
            String fieldName = field.getName();
            JsonNode fieldValue = null;
            switch (field.getType()) {
                case NESTED:
                    fieldValue = mappingNestedNode(fieldMap, field, sourceNode, destPrefix, sourcePrefix, doValid);
                    break;

                default:
                    CurrentNode currentNode = getCurrentNode(fieldMap, field, sourceNode, fieldName, destPrefix, sourcePrefix);
                    fieldValue = mappingPrimitiveNode(currentNode, field.isArrayType());
                    break;
            }

            if ((fieldValue == null || fieldValue.isNull() || (fieldValue.isArray() && fieldValue.size() == 0))) {
                fieldValue = field.loadDefaultJsonNode();
            }

            if (doValid) {
                JsonNodeTypeValidator.validate(field, fieldValue, destPrefix);
            }
            if (null != fieldValue) {
                destNode.set(fieldName, fieldValue);
            }
        });

        return destNode;
    }

    private static JsonNode mappingNestedNode(Map<String, DynamicModelMapping> fieldMap, CommonModelField field, ObjectNode sourceNode, String destPrefix, String sourcePrefix, boolean doValid) {
        List<CommonModelField> dynamicModelFieldList = field.getFields();
        destPrefix = destPrefix.equals("") ? field.getName() : destPrefix + "." + field.getName();
        if (field.isArrayType()) {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            CurrentArray currentArray = getCurrentArray(fieldMap, sourceNode, dynamicModelFieldList, destPrefix, sourcePrefix);
            if (currentArray != null) {
                for (JsonNode jsonNode : currentArray.getArray()) {
                    if (!jsonNode.isNull()) {
                        JsonNode jsonValue = mappingObject(fieldMap, (ObjectNode) jsonNode, JsonNodeFactory.instance.objectNode(), dynamicModelFieldList, destPrefix, currentArray.getPrefix(), doValid);
                        if (jsonValue != null) {
                            arrayNode.add(jsonValue);
                        }
                    }
                }
            }
            return arrayNode;
        } else {
            return mappingObject(fieldMap, sourceNode, JsonNodeFactory.instance.objectNode(), dynamicModelFieldList, destPrefix, sourcePrefix, doValid);
        }
    }

    private static CurrentArray getCurrentArray(Map<String, DynamicModelMapping> fieldMap, JsonNode sourceNode, List<CommonModelField> dynamicModelFieldList, String prefixName, String sourcePrefix) {
        String firstFieldName = dynamicModelFieldList.get(0).getName();
        String firstFieldFullName = prefixName.equals("") ? firstFieldName : prefixName + "." + firstFieldName;

        DynamicModelMapping dynamicModelMapping = fieldMap.get(firstFieldFullName);

        if (dynamicModelMapping == null) {
            return null;
        }

        String sourceFieldName = dynamicModelMapping.getSourceFieldName();
        if (!sourcePrefix.equals("") && sourceFieldName.startsWith(sourcePrefix)) {
            sourceFieldName = sourceFieldName.substring(sourcePrefix.length());
        }
        String[] fieldNames = StringUtils.split(sourceFieldName, ".");
        String prefix = sourcePrefix;

        if (fieldNames == null) return null;

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        if (fieldNames.length == 1) {
            arrayNode.add(sourceNode);
        } else {
            JsonNode jsonNode = sourceNode;
            for (int i = 0; i < fieldNames.length - 1; i++) {
                String field = fieldNames[i];
                jsonNode = jsonNode.get(field);
                if (jsonNode == null) {
                    break;
                }
                prefix += field + ".";
            }

            if (jsonNode == null) {
                return CurrentArray.from(arrayNode, prefix);
            }

            if (jsonNode.isArray()) {
                return CurrentArray.from(jsonNode, prefix);
            } else {
                arrayNode.add(jsonNode);
            }
        }

        return CurrentArray.from(arrayNode, prefix);
    }

    private static JsonNode mappingPrimitiveNode(CurrentNode currentNode, boolean isArray) {
        if (currentNode == null) {
            return null;
        }
        return currentNode.getValue(isArray);
    }

    private static CurrentNode getCurrentNode(Map<String, DynamicModelMapping> fieldMap, DynamicModelField dataModelField, JsonNode sourceNode, String fieldName, String destPrefix, String sourcePrefix) {
        String fieldFullName = destPrefix.equals("") ? fieldName : destPrefix + "." + fieldName;
        DynamicModelMapping dataModelMapping = fieldMap.get(fieldFullName);

        if (dataModelMapping == null) {
            return null;
        }

        if (dataModelMapping.getMappingType().equals(DynamicFieldMappingType.FIXED_VALUE)) {
            return CurrentNode.from(dataModelField, dataModelMapping, null, null);
        } else if (dataModelMapping.getMappingType().equals(DynamicFieldMappingType.FROM_MULTI_SOURCE_FIELD)) {
            return CurrentNode.from(dataModelField, dataModelMapping, sourceNode, null);
        } else if (dataModelMapping.getMappingType().equals(DynamicFieldMappingType.CONDITION_EXP)) {
            return CurrentNode.from(dataModelField, dataModelMapping, sourceNode, null);
        }

        String sourceFieldName = dataModelMapping.getSourceFieldName();
        if (!sourcePrefix.equals("") && sourceFieldName.startsWith(sourcePrefix)) {
            sourceFieldName = sourceFieldName.substring(sourcePrefix.length());
        }

        String[] fieldNames = StringUtils.split(sourceFieldName, ".");
        if (fieldNames == null) return null;

        if (fieldNames.length == 1) {
            return CurrentNode.from(dataModelField, dataModelMapping, sourceNode, sourceFieldName);
        } else {
            JsonNode jsonNode = sourceNode;
            for (int i = 0; i < fieldNames.length - 1; i++) {
                if (jsonNode.isArray()) {
                    jsonNode = jsonNode.get(0);
                    if (jsonNode == null) {
                        break;
                    }
                }

                String field = fieldNames[i];
                jsonNode = jsonNode.get(field);
                if (jsonNode == null) {
                    break;
                }
            }

            if (jsonNode == null) {
                return null;
            }

            return CurrentNode.from(dataModelField, dataModelMapping, jsonNode, fieldNames[fieldNames.length - 1]);
        }
    }
}