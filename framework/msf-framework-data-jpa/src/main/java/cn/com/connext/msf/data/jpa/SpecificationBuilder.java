package cn.com.connext.msf.data.jpa;

import cn.com.connext.msf.framework.entity.ConnextEntityFieldInfo;
import cn.com.connext.msf.framework.entity.ConnextEntityInfo;
import cn.com.connext.msf.framework.entity.ConnextEntityManager;
import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.query.*;
import cn.com.connext.msf.framework.utils.Time;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

public class SpecificationBuilder {

    public static <T> Specification<T> build(QueryInfo queryInfo) {
        if (StringUtils.isEmpty(queryInfo.getExpression())) {
            return null;
        }

        List<QueryExpression> expressionNodes = QueryExpression.parse(queryInfo.getExpression());
        return build(expressionNodes, queryInfo.getCheckOperator());
    }


    public static <T> Specification<T> build(List<QueryExpression> expressionNodes, boolean checkOperator) {
        return (root, query, cb) -> {
            ConnextEntityInfo connextEntityInfo = ConnextEntityManager.getConnextEntityInfo(root.getModel().getBindableJavaType());
            Predicate predicate = null;
            QueryRelationType relationType = null;

            for (QueryExpression node : expressionNodes) {
                Predicate tmpPredicate = null;
                if (node.nodes.size() > 0) {
                    Specification<T> childSpecification = build(node.nodes, checkOperator);
                    tmpPredicate = childSpecification.toPredicate(root, query, cb);
                } else {
                    tmpPredicate = buildPredicate(connextEntityInfo, root, cb, node.expression, checkOperator);
                }

                if (predicate == null) {
                    predicate = tmpPredicate;
                } else {
                    if (relationType == QueryRelationType.AND) {
                        predicate = cb.and(predicate, tmpPredicate);
                    } else {
                        predicate = cb.or(predicate, tmpPredicate);
                    }
                }
                relationType = node.queryRelationType;
            }
            return query.where(predicate).getRestriction();
        };
    }

    private static <T> Predicate buildPredicate(ConnextEntityInfo connextEntityInfo, Root<T> root, CriteriaBuilder cb, String expression, boolean checkOperator) {
        QueryExpressionInfo expressionInfo = QueryExpressionInfo.from(expression);

        String fieldName = expressionInfo.getFieldName();
        String operator = expressionInfo.getOperator();
        String fieldValue = expressionInfo.getFieldValue();

        ConnextEntityFieldInfo fieldInfo = connextEntityInfo.getField(fieldName);

        if (fieldInfo == null) {
            throw new BusinessException(MessageFormat.format("Invalid field \"{0}\", expression: {1}.", fieldName, expression));
        }

        if (checkOperator && !fieldInfo.getOperators().contains(operator)) {
            throw new BusinessException(MessageFormat.format("{0} field does not support {1} operator.", fieldName, operator, expression));
        }

        String fieldType = fieldInfo.getType();

        switch (operator) {
            case QueryOperators.EQ:
                return buildEqual(root, cb, fieldName, fieldType, fieldValue);

            case QueryOperators.NE:
                return buildNotEqual(root, cb, fieldName, fieldType, fieldValue);

            case QueryOperators.LIKE:
                return buildLike(root, cb, fieldName, fieldType, fieldValue);

            case QueryOperators.GT:
                return buildGreaterThen(root, cb, fieldName, fieldType, fieldValue);

            case QueryOperators.GE:
                return buildGreaterEqual(root, cb, fieldName, fieldType, fieldValue);

            case QueryOperators.LT:
                return buildLessThen(root, cb, fieldName, fieldType, fieldValue);

            case QueryOperators.LE:
                return buildLessEqual(root, cb, fieldName, fieldType, fieldValue);
        }

        throw new RuntimeException(MessageFormat.format("Invalid query expression, {0} operator not support. ", operator));

    }

    private static <T> Predicate buildEqual(Root<T> root, CriteriaBuilder cb, String fieldName, String fieldType, String fieldValue) {

        switch (fieldType) {
            case "java.lang.String":
                Path<String> stringPath = root.get(fieldName);
                return cb.equal(stringPath, fieldValue);

            case "java.lang.Boolean":
            case "boolean":
                Path<Boolean> booleanPath = root.get(fieldName);
                return cb.equal(booleanPath, Boolean.parseBoolean(fieldValue));

            case "java.lang.Byte":
            case "byte":
                Path<Byte> bytePath = root.get(fieldName);
                return cb.equal(bytePath, Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                Path<Short> shortPath = root.get(fieldName);
                return cb.equal(shortPath, Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                Path<Integer> intPath = root.get(fieldName);
                return cb.equal(intPath, Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                Path<Long> longPath = root.get(fieldName);
                return cb.equal(longPath, Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                Path<Float> floatPath = root.get(fieldName);
                return cb.equal(floatPath, Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                Path<Double> doublePath = root.get(fieldName);
                return cb.equal(doublePath, Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                Path<Date> datePath = root.get(fieldName);
                return cb.equal(datePath, Time.parseDate(fieldValue));

        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));

    }

    private static <T> Predicate buildNotEqual(Root<T> root, CriteriaBuilder cb, String fieldName, String fieldType, String fieldValue) {

        switch (fieldType) {
            case "java.lang.String":
                Path<String> stringPath = root.get(fieldName);
                return cb.notEqual(stringPath, fieldValue);

            case "java.lang.Boolean":
            case "boolean":
                Path<Boolean> booleanPath = root.get(fieldName);
                return cb.notEqual(booleanPath, Boolean.parseBoolean(fieldValue));

            case "java.lang.Byte":
            case "byte":
                Path<Byte> bytePath = root.get(fieldName);
                return cb.notEqual(bytePath, Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                Path<Short> shortPath = root.get(fieldName);
                return cb.notEqual(shortPath, Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                Path<Integer> intPath = root.get(fieldName);
                return cb.notEqual(intPath, Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                Path<Long> longPath = root.get(fieldName);
                return cb.notEqual(longPath, Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                Path<Float> floatPath = root.get(fieldName);
                return cb.notEqual(floatPath, Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                Path<Double> doublePath = root.get(fieldName);
                return cb.notEqual(doublePath, Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                Path<Date> datePath = root.get(fieldName);
                return cb.notEqual(datePath, Time.parseDate(fieldValue));

        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));

    }

    private static <T> Predicate buildLike(Root<T> root, CriteriaBuilder cb, String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.String":
                Path<String> stringPath = root.get(fieldName);
                return cb.like(stringPath, "%" + fieldValue + "%");
        }
        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static <T> Predicate buildGreaterThen(Root<T> root, CriteriaBuilder cb, String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.Byte":
            case "byte":
                Path<Byte> bytePath = root.get(fieldName);
                return cb.greaterThan(bytePath, Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                Path<Short> shortPath = root.get(fieldName);
                return cb.greaterThan(shortPath, Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                Path<Integer> intPath = root.get(fieldName);
                return cb.greaterThan(intPath, Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                Path<Long> longPath = root.get(fieldName);
                return cb.greaterThan(longPath, Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                Path<Float> floatPath = root.get(fieldName);
                return cb.greaterThan(floatPath, Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                Path<Double> doublePath = root.get(fieldName);
                return cb.greaterThan(doublePath, Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                Path<Date> datePath = root.get(fieldName);
                return cb.greaterThan(datePath, Time.parseDate(fieldValue));
        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static <T> Predicate buildGreaterEqual(Root<T> root, CriteriaBuilder cb, String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.Byte":
            case "byte":
                Path<Byte> bytePath = root.get(fieldName);
                return cb.greaterThanOrEqualTo(bytePath, Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                Path<Short> shortPath = root.get(fieldName);
                return cb.greaterThanOrEqualTo(shortPath, Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                Path<Integer> intPath = root.get(fieldName);
                return cb.greaterThanOrEqualTo(intPath, Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                Path<Long> longPath = root.get(fieldName);
                return cb.greaterThanOrEqualTo(longPath, Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                Path<Float> floatPath = root.get(fieldName);
                return cb.greaterThanOrEqualTo(floatPath, Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                Path<Double> doublePath = root.get(fieldName);
                return cb.greaterThanOrEqualTo(doublePath, Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                Path<Date> datePath = root.get(fieldName);
                return cb.greaterThanOrEqualTo(datePath, Time.parseDate(fieldValue));
        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static <T> Predicate buildLessThen(Root<T> root, CriteriaBuilder cb, String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.Byte":
            case "byte":
                Path<Byte> bytePath = root.get(fieldName);
                return cb.lessThan(bytePath, Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                Path<Short> shortPath = root.get(fieldName);
                return cb.lessThan(shortPath, Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                Path<Integer> intPath = root.get(fieldName);
                return cb.lessThan(intPath, Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                Path<Long> longPath = root.get(fieldName);
                return cb.lessThan(longPath, Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                Path<Float> floatPath = root.get(fieldName);
                return cb.lessThan(floatPath, Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                Path<Double> doublePath = root.get(fieldName);
                return cb.lessThan(doublePath, Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                Path<Date> datePath = root.get(fieldName);
                return cb.lessThan(datePath, Time.parseDate(fieldValue));
        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

    private static <T> Predicate buildLessEqual(Root<T> root, CriteriaBuilder cb, String fieldName, String fieldType, String fieldValue) {
        switch (fieldType) {
            case "java.lang.Byte":
            case "byte":
                Path<Byte> bytePath = root.get(fieldName);
                return cb.lessThanOrEqualTo(bytePath, Byte.parseByte(fieldValue));

            case "java.lang.Short":
            case "short":
                Path<Short> shortPath = root.get(fieldName);
                return cb.lessThanOrEqualTo(shortPath, Short.parseShort(fieldValue));

            case "java.lang.Integer":
            case "int":
                Path<Integer> intPath = root.get(fieldName);
                return cb.lessThanOrEqualTo(intPath, Integer.parseInt(fieldValue));

            case "java.lang.Long":
            case "long":
                Path<Long> longPath = root.get(fieldName);
                return cb.lessThanOrEqualTo(longPath, Long.parseLong(fieldValue));

            case "java.lang.Float":
            case "float":
                Path<Float> floatPath = root.get(fieldName);
                return cb.lessThanOrEqualTo(floatPath, Float.parseFloat(fieldValue));

            case "java.lang.Double":
            case "double":
                Path<Double> doublePath = root.get(fieldName);
                return cb.lessThanOrEqualTo(doublePath, Double.parseDouble(fieldValue));

            case "java.util.Date":
            case "java.sql.Date":
                Path<Date> datePath = root.get(fieldName);
                return cb.lessThanOrEqualTo(datePath, Time.parseDate(fieldValue));
        }

        throw new RuntimeException(MessageFormat.format("Invalid query type, {0} type not support. ", fieldType));
    }

}
