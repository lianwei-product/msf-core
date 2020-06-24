package cn.com.connext.msf.data.mongo;

import cn.com.connext.msf.framework.entity.ConnextEntityFieldInfo;
import cn.com.connext.msf.framework.entity.ConnextEntityInfo;
import cn.com.connext.msf.framework.entity.ConnextEntityManager;
import cn.com.connext.msf.framework.query.*;
import cn.com.connext.msf.framework.utils.Time;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {

    private static List<String> regexKeywordList = Arrays.asList("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|");

    public static Query build(Class classInfo, String expression, Object... arguments) {
        return build(classInfo, QueryInfo.from(expression, arguments));
    }

    public static Query build(Class classInfo, QueryInfo queryInfo) {
        ConnextEntityInfo connextEntityInfo = ConnextEntityManager.getConnextEntityInfo(classInfo);
        return build(connextEntityInfo, queryInfo);
    }

    public static Query build(ConnextEntityInfo connextEntityInfo, QueryInfo queryInfo) {
        if (queryInfo == null) {
            return null;
        }

        if (StringUtils.isEmpty(queryInfo.getExpression())) {
            return null;
        }

        List<QueryExpression> expressionNodes = QueryExpression.parse(queryInfo.getExpression());
        Criteria criteria = buildCriteria(connextEntityInfo, expressionNodes, queryInfo.getCheckOperator());
        return new Query(criteria);
    }


    private static Criteria buildCriteria(ConnextEntityInfo connextEntityInfo, List<QueryExpression> expressionNodes, boolean checkOperator) {
        Criteria criteria = new Criteria();
        List<Criteria> childList = Lists.newArrayList();

        for (QueryExpression node : expressionNodes) {
            Criteria childCriteria;
            if (node.nodes.size() > 0) {
                childCriteria = buildCriteria(connextEntityInfo, node.nodes, checkOperator);
            } else {
                childCriteria = buildCriteria(connextEntityInfo, node.expression);
            }
            childList.add(childCriteria);
        }

        Criteria[] childArray = new Criteria[childList.size()];
        childArray = childList.toArray(childArray);

        QueryRelationType relationType = expressionNodes.get(0).queryRelationType;
        if (relationType == QueryRelationType.AND) {
            criteria.andOperator(childArray);
        } else {
            criteria.orOperator(childArray);
        }

        return criteria;
    }

    private static Criteria buildCriteria(ConnextEntityInfo connextEntityInfo, String expression) {
        QueryExpressionInfo expressionInfo = QueryExpressionInfo.from(expression);

        String fieldName = expressionInfo.getFieldName();
        String operator = expressionInfo.getOperator();
        String fieldValue = expressionInfo.getFieldValue();

        ConnextEntityFieldInfo fieldInfo = connextEntityInfo.getField(fieldName);
        if (fieldInfo == null) {
            throw new RuntimeException(MessageFormat.format("Invalid field name: {0}", fieldName));
        }
        String fieldType = fieldInfo.getType();

        switch (operator) {
            case QueryOperators.EQ:
                return buildEqual(fieldName, fieldType, fieldValue);

            case QueryOperators.NE:
                return buildNotEqual(fieldName, fieldType, fieldValue);

            case QueryOperators.LIKE:
                return buildLike(fieldName, fieldType, fieldValue);

            case QueryOperators.IN:
                return buildIn(fieldName, fieldType, fieldValue);

            case QueryOperators.NIN:
                return buildNotIn(fieldName, fieldType, fieldValue);

            case QueryOperators.GT:
                return buildGreaterThen(fieldName, fieldType, fieldValue);

            case QueryOperators.GE:
                return buildGreaterEqual(fieldName, fieldType, fieldValue);

            case QueryOperators.LT:
                return buildLessThen(fieldName, fieldType, fieldValue);

            case QueryOperators.LE:
                return buildLessEqual(fieldName, fieldType, fieldValue);
        }

        throw new RuntimeException(MessageFormat.format("Invalid query expression, {0} operator not support. ", operator));
    }

    private static Criteria buildEqual(String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.String":
                return Criteria.where(fieldName).is(fieldValue);

            case "java.lang.Boolean":
            case "boolean":
                return Criteria.where(fieldName).is(Boolean.parseBoolean(fieldValue));

            case "java.lang.Byte":
            case "byte":
                return Criteria.where(fieldName).is(Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                return Criteria.where(fieldName).is(Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                return Criteria.where(fieldName).is(Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                return Criteria.where(fieldName).is(Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                return Criteria.where(fieldName).is(Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                return Criteria.where(fieldName).is(Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                return Criteria.where(fieldName).is(Time.parseDate(fieldValue));

            case "java.time.LocalDate":
                return Criteria.where(fieldName).is(LocalDate.parse(fieldValue));

            case "java.time.ZonedDateTime":
                return Criteria.where(fieldName).is(ZonedDateTime.parse(fieldValue));

            case "java.util.List":
                return Criteria.where(fieldName).in(fieldValue);

        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static Criteria buildNotEqual(String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.String":
                return Criteria.where(fieldName).ne(fieldValue);

            case "java.lang.Boolean":
            case "boolean":
                return Criteria.where(fieldName).ne(Boolean.parseBoolean(fieldValue));

            case "java.lang.Byte":
            case "byte":
                return Criteria.where(fieldName).ne(Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                return Criteria.where(fieldName).ne(Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                return Criteria.where(fieldName).ne(Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                return Criteria.where(fieldName).ne(Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                return Criteria.where(fieldName).ne(Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                return Criteria.where(fieldName).ne(Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                return Criteria.where(fieldName).ne(Time.parseDate(fieldValue));
            case "java.time.LocalDate":
                return Criteria.where(fieldName).ne(LocalDate.parse(fieldValue));
            case "java.time.ZonedDateTime":
                return Criteria.where(fieldName).ne(ZonedDateTime.parse(fieldValue));

            case "java.util.List":
                return Criteria.where(fieldName).nin(fieldValue);
        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static Criteria buildLike(String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.String":
                return Criteria.where(fieldName).regex(".*?" + rebuildRegexFieldValue(fieldValue) + ".*");
        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static Criteria buildIn(String fieldName, String fieldType, String fieldValue) {
        List<Object> fieldValueList = getValueList(fieldType, fieldValue);
        return Criteria.where(fieldName).in(fieldValueList);
    }

    private static Criteria buildNotIn(String fieldName, String fieldType, String fieldValue) {
        List<Object> fieldValueList = getValueList(fieldType, fieldValue);
        return Criteria.where(fieldName).nin(fieldValueList);
    }

    private static Criteria buildGreaterThen(String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.String":
                return Criteria.where(fieldName).gt(fieldValue);

            case "java.lang.Boolean":
            case "boolean":
                return Criteria.where(fieldName).gt(Boolean.parseBoolean(fieldValue));

            case "java.lang.Byte":
            case "byte":
                return Criteria.where(fieldName).gt(Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                return Criteria.where(fieldName).gt(Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                return Criteria.where(fieldName).gt(Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                return Criteria.where(fieldName).gt(Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                return Criteria.where(fieldName).gt(Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                return Criteria.where(fieldName).gt(Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                return Criteria.where(fieldName).gt(Time.parseDate(fieldValue));
            case "java.time.LocalDate":
                return Criteria.where(fieldName).gt(LocalDate.parse(fieldValue));
            case "java.time.ZonedDateTime":
                return Criteria.where(fieldName).gt(ZonedDateTime.parse(fieldValue));

        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static Criteria buildGreaterEqual(String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.String":
                return Criteria.where(fieldName).gte(fieldValue);

            case "java.lang.Boolean":
            case "boolean":
                return Criteria.where(fieldName).gte(Boolean.parseBoolean(fieldValue));

            case "java.lang.Byte":
            case "byte":
                return Criteria.where(fieldName).gte(Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                return Criteria.where(fieldName).gte(Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                return Criteria.where(fieldName).gte(Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                return Criteria.where(fieldName).gte(Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                return Criteria.where(fieldName).gte(Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                return Criteria.where(fieldName).gte(Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                return Criteria.where(fieldName).gte(Time.parseDate(fieldValue));
            case "java.time.LocalDate":
                return Criteria.where(fieldName).gte(LocalDate.parse(fieldValue));
            case "java.time.ZonedDateTime":
                return Criteria.where(fieldName).gte(ZonedDateTime.parse(fieldValue));

        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static Criteria buildLessThen(String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.String":
                return Criteria.where(fieldName).lt(fieldValue);

            case "java.lang.Boolean":
            case "boolean":
                return Criteria.where(fieldName).lt(Boolean.parseBoolean(fieldValue));

            case "java.lang.Byte":
            case "byte":
                return Criteria.where(fieldName).lt(Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                return Criteria.where(fieldName).lt(Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                return Criteria.where(fieldName).lt(Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                return Criteria.where(fieldName).lt(Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                return Criteria.where(fieldName).lt(Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                return Criteria.where(fieldName).lt(Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                return Criteria.where(fieldName).lt(Time.parseDate(fieldValue));
            case "java.time.LocalDate":
                return Criteria.where(fieldName).lt(LocalDate.parse(fieldValue));
            case "java.time.ZonedDateTime":
                return Criteria.where(fieldName).lt(ZonedDateTime.parse(fieldValue));

        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static Criteria buildLessEqual(String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.String":
                return Criteria.where(fieldName).lte(fieldValue);

            case "java.lang.Boolean":
            case "boolean":
                return Criteria.where(fieldName).lte(Boolean.parseBoolean(fieldValue));

            case "java.lang.Byte":
            case "byte":
                return Criteria.where(fieldName).lte(Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                return Criteria.where(fieldName).lte(Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                return Criteria.where(fieldName).lte(Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                return Criteria.where(fieldName).lte(Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                return Criteria.where(fieldName).lte(Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                return Criteria.where(fieldName).lte(Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                return Criteria.where(fieldName).lte(Time.parseDate(fieldValue));
            case "java.time.LocalDate":
                return Criteria.where(fieldName).lte(LocalDate.parse(fieldValue));
            case "java.time.ZonedDateTime":
                return Criteria.where(fieldName).lte(ZonedDateTime.parse(fieldValue));

        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static String rebuildRegexFieldValue(String fieldValue) {
        for (String keyword : regexKeywordList) {

            if (fieldValue.contains(keyword)) {
                fieldValue = StringUtils.replace(fieldValue, keyword, "\\".concat(keyword));
            }
        }
        return fieldValue;
    }

    private static List<Object> getValueList(String fieldType, String fieldValue) {
        List<Object> resultList = Lists.newArrayList();
        String[] itemValueList = StringUtils.split(fieldValue, ",");
        for (String itemValue : itemValueList) {
            resultList.add(getValue(fieldType, itemValue));
        }
        return resultList;
    }

    private static Object getValue(String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.Boolean":
            case "boolean":
                return Boolean.parseBoolean(fieldValue);

            case "java.lang.Byte":
            case "byte":
                return Byte.parseByte(fieldValue);

            case "java.lang.Short":
            case "short":
            case "java.lang.Integer":
            case "int":
            case "java.lang.Long":
            case "long":
            case "java.lang.Float":
            case "float":
            case "java.lang.Double":
            case "double":
                return Double.parseDouble(fieldValue);

            case "java.util.Date":
            case "java.sql.Date":
                return Time.parseDate(fieldValue);

            case "java.time.LocalDate":
                return LocalDate.parse(fieldValue);

            case "java.time.LocalDateTime":
                return LocalDateTime.parse(fieldValue);

            case "java.time.ZonedDateTime":
                return ZonedDateTime.parse(fieldValue);

            case "java.lang.String":
                return fieldValue;
        }
        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }
}
