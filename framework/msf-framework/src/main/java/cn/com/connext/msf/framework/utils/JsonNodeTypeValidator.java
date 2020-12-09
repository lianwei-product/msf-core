package cn.com.connext.msf.framework.utils;

import cn.com.connext.msf.framework.dynamic.DynamicModelField;
import cn.com.connext.msf.framework.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.util.StringUtils;

public class JsonNodeTypeValidator {

    public static void validate(DynamicModelField field, JsonNode fieldValue, String prefixName) {
        if (field.isArrayType()) {
            validateArrayNodeType(field, fieldValue, prefixName);
        } else {
            validateObjectNodeType(field, fieldValue, prefixName);
        }
    }

    private static void validateArrayNodeType(DynamicModelField field, JsonNode fieldValue, String prefixName) {
        if (!field.isAllowNull()) {
            assertValueNotNull(field, fieldValue, prefixName);
        } else {
            if (fieldValue == null || fieldValue.isNull()) {
                return;
            }
        }

        assertTypeIsCorrect(fieldValue.isArray(), field, fieldValue, prefixName);
        ArrayNode arrayNode = (ArrayNode) fieldValue;

        arrayNode.forEach(node -> validateObjectNodeType(field, node, prefixName));
    }


    private static void validateObjectNodeType(DynamicModelField field, JsonNode fieldValue, String prefixName) {
        if (!field.isAllowNull()) {
            assertValueNotNull(field, fieldValue, prefixName);
        } else {
            if (fieldValue == null || fieldValue.isNull()) {
                return;
            }
        }

        boolean typeIsCorrect = true;
        switch (field.getType()) {
            case KEYWORD:
            case TEXT:
                typeIsCorrect = fieldValue.isTextual();
                break;

            case SHORT:
                typeIsCorrect = fieldValue.isNumber() && Validator.isShort(fieldValue.asText(null));
                break;

            case INTEGER:
                typeIsCorrect = fieldValue.isNumber() && Validator.isInteger(fieldValue.asText(null));
                break;

            case LONG:
                typeIsCorrect = fieldValue.isNumber() && Validator.isLong(fieldValue.asText(null));
                break;

            case DOUBLE:
                typeIsCorrect = fieldValue.isNumber() && Validator.isDouble(fieldValue.asText(null));
                break;

            case FLOAT:
                typeIsCorrect = fieldValue.isNumber() && Validator.isFloat(fieldValue.asText(null));
                break;

            case DATE:
                typeIsCorrect = Validator.isDate(fieldValue.asText(null));
                break;

            case BOOLEAN:
                typeIsCorrect = fieldValue.isBoolean();
                break;

            case NESTED:
                typeIsCorrect = fieldValue.isObject();
                break;
        }

        assertTypeIsCorrect(typeIsCorrect, field, fieldValue, prefixName);
    }

    private static void assertValueNotNull(DynamicModelField field, JsonNode fieldValue, String prefixName) {
        if (fieldValue == null || fieldValue.isNull()) {
            String fieldFullName = StringUtils.isEmpty(prefixName) ? field.getName() : prefixName + "." + field.getName();
            if (field.isArrayType()) {
                throw new BusinessException("field_not_allow_null", "fieldName: " + fieldFullName + ", fieldType: Array<" + field.getType() + ">");
            } else {
                throw new BusinessException("field_not_allow_null", "fieldName: " + fieldFullName + ", fieldType: " + field.getType());
            }
        }
    }

    private static void assertTypeIsCorrect(boolean typeIsCorrect, DynamicModelField field, JsonNode fieldValue, String prefixName) {
        //System.out.println(fieldValue.getNodeType().name());
        if (typeIsCorrect) return;
        String fieldFullName = StringUtils.isEmpty(prefixName) ? field.getName() : prefixName + "." + field.getName();
        if (field.isArrayType()) {
            throw new BusinessException("invalid_field_type_" + field.getType(), "fieldName: " + fieldFullName + ", fieldType: Array<" + field.getType() + ">, fieldValue: " + fieldValue.toString());
        } else {
            throw new BusinessException("invalid_field_type_" + field.getType(), "fieldName: " + fieldFullName + ", fieldType: " + field.getType() + ", fieldValue: " + fieldValue.toString());
        }
    }

    //用于文件导入数据或者数据抽取字段值验证(暂不支持嵌套类型)
    public static void validateFieldValueByDataType(DynamicModelField field, String fieldValue, String prefixName) {
        if (!field.isAllowNull()) {
            assertValueNotNull(field, fieldValue, prefixName);
        } else if (StringUtils.isEmpty(fieldValue)) {
            return;
        }

        boolean typeIsCorrect = true;
        switch (field.getType()) {

            case SHORT:
                typeIsCorrect = Validator.isShort(fieldValue);
                break;

            case INTEGER:
                typeIsCorrect = Validator.isInteger(fieldValue);
                break;

            case LONG:
                typeIsCorrect = Validator.isLong(fieldValue);
                break;

            case DOUBLE:
                typeIsCorrect = Validator.isDouble(fieldValue);
                break;

            case FLOAT:
                typeIsCorrect = Validator.isFloat(fieldValue);
                break;

            case DATE:
                typeIsCorrect = Validator.isDate(fieldValue);
                break;

            case BOOLEAN:
                typeIsCorrect = Validator.isBoolean(fieldValue);
                break;
            case NESTED:
                typeIsCorrect = false;
        }

        assertTypeIsCorrect(typeIsCorrect, field, fieldValue, prefixName);
    }

    private static void assertValueNotNull(DynamicModelField field, String fieldValue, String prefixName) {
        if (Validator.isEmpty(fieldValue)) {
            String fieldFullName = StringUtils.isEmpty(prefixName) ? field.getName() : prefixName + "." + field.getName();
            if (field.isArrayType()) {
                throw new BusinessException("field_not_allow_null", "fieldName: " + fieldFullName + ", fieldType: Array<" + field.getType() + ">");
            } else {
                throw new BusinessException("field_not_allow_null", "fieldName: " + fieldFullName + ", fieldType: " + field.getType());
            }
        }
    }

    private static void assertTypeIsCorrect(boolean typeIsCorrect, DynamicModelField field, String fieldValue, String prefixName) {
        if (typeIsCorrect) return;
        String fieldFullName = StringUtils.isEmpty(prefixName) ? field.getName() : prefixName + "." + field.getName();
        if (field.isArrayType()) {
            throw new BusinessException("invalid_field_type_" + field.getType(), "fieldName: " + fieldFullName + ", fieldType: Array<" + field.getType() + ">, fieldValue: " + fieldValue);
        } else {
            throw new BusinessException("invalid_field_type_" + field.getType(), "fieldName: " + fieldFullName + ", fieldType: " + field.getType() + ", fieldValue: " + fieldValue);
        }
    }
}
