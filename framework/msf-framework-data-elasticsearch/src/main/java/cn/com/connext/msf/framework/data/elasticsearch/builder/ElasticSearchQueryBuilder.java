package cn.com.connext.msf.framework.data.elasticsearch.builder;

import cn.com.connext.msf.framework.data.elasticsearch.utils.NestedPath;
import cn.com.connext.msf.framework.entity.ConnextEntityFieldInfo;
import cn.com.connext.msf.framework.entity.ConnextEntityInfo;
import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.query.*;
import cn.com.connext.msf.framework.utils.MonthPeriod;
import cn.com.connext.msf.framework.utils.Time;
import cn.com.connext.msf.framework.utils.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Sort;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class ElasticSearchQueryBuilder {

    private final static String KEYWORD = "keyword";
    private final static String TEXT = "text";
    private final static DateTimeFormatter ES_ISO_DATETIME_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static QueryBuilder build(ConnextEntityInfo connextEntityInfo, QueryInfo queryInfo) {
        if (queryInfo == null) {
            return null;
        }

        Sort sort = queryInfo.getPageable().getSort();
        for (Sort.Order order : sort) {
            String fieldName = order.getProperty();
            ConnextEntityFieldInfo fieldInfo = connextEntityInfo.getField(fieldName);
            if (fieldInfo == null || !fieldInfo.isAllowSort()) {
                throw new BusinessException(MessageFormat.format("Invalid sort field \"{0}\".", fieldName));
            }
        }

        if (StringUtils.isEmpty(queryInfo.getExpression())) {
            return null;
        }

        List<QueryExpression> expressionNodes = QueryExpression.parse(queryInfo.getExpression());
        return build(connextEntityInfo, expressionNodes);
    }

    private static QueryBuilder build(ConnextEntityInfo connextEntityInfo, List<QueryExpression> expressionNodes) {
        BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();
        for (QueryExpression node : expressionNodes) {
            QueryBuilder tmpQuery;
            if (node.nodes.size() > 0) {
                tmpQuery = build(connextEntityInfo, node.nodes);
            } else {
                QueryExpressionInfo queryExpressionInfo = QueryExpressionInfo.from(node.expression);
                tmpQuery = build(connextEntityInfo, queryExpressionInfo);
            }

            QueryRelationType relationType = expressionNodes.get(0).queryRelationType;
            if (relationType == QueryRelationType.AND) {
                rootQuery.must(tmpQuery);
            } else {
                rootQuery.should(tmpQuery);
            }
        }
        return rootQuery;
    }

    private static QueryBuilder build(ConnextEntityInfo connextEntityInfo, QueryExpressionInfo queryExpressionInfo) {
        String fieldName = queryExpressionInfo.getFieldName();
        String operator = queryExpressionInfo.getOperator();
        String fieldValue = queryExpressionInfo.getFieldValue();

        ConnextEntityFieldInfo fieldInfo = connextEntityInfo.getField(fieldName);
        if (fieldInfo == null) {
            throw new BusinessException(MessageFormat.format("Invalid field \"{0}\".", fieldName));
        }

        if (!fieldInfo.getOperators().contains(operator)) {
            throw new BusinessException(MessageFormat.format("Field {0} does not support {1} operator.", fieldName, operator));
        }
        String fieldType = fieldInfo.getType();


        return buildQueryBuilder(operator, fieldType, fieldName, fieldValue);
    }

    private static String[] getValues(String fieldValue) {
        return StringUtils.split(fieldValue, ",");
    }

    public static QueryBuilder buildQueryBuilder(String operator, String fieldType, String fieldName, String fieldValue) {
        if (Validator.isDate(fieldValue) && (Objects.equals("date", fieldType) || Objects.equals("java.util.Date", fieldType) || Objects.equals("java.sql.Date", fieldType))) {
            LocalDateTime localDateTime = Time.parseLocalDateTime(fieldValue, TimeZone.getDefault().getID());
            fieldValue = localDateTime.atZone(ZoneId.systemDefault()).format(ES_ISO_DATETIME_FORMATER);
        }

        String nestedPath = NestedPath.getNestedPath(fieldName);
        switch (operator) {
            case QueryOperators.LT:
                return buildLessThen(fieldName, fieldValue, nestedPath);

            case QueryOperators.LE:
                return buildLessEqual(fieldName, fieldValue, nestedPath);

            case QueryOperators.GT:
                return buildGreaterThen(fieldName, fieldValue, nestedPath);

            case QueryOperators.GE:
                return buildGreaterEqual(fieldName, fieldValue, nestedPath);

            case QueryOperators.BF:
                return buildBefore(fieldName, fieldValue, nestedPath);

            case QueryOperators.AF:
                return buildAfter(fieldName, fieldValue, nestedPath);

            case QueryOperators.MONTH_PERIOD:
                return buildMonthPeriod(fieldName, fieldValue, nestedPath);

            case QueryOperators.NE:
                return buildNotEqual(fieldName, fieldValue, nestedPath);

            case QueryOperators.EMPTY:
                return buildEmpty(fieldName, fieldType, fieldValue, nestedPath);

            case QueryOperators.LIKE:
                return buildLike(fieldName, fieldValue, nestedPath);

            default: // EQ
                return buildEqual(fieldName, fieldValue, nestedPath);
        }
    }


    private static QueryBuilder buildEqual(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder neQueryBuilder = QueryBuilders.termsQuery(fieldName, getValues(fileValue));
        if (StringUtils.isNotEmpty(nestedPath)) {
            neQueryBuilder = QueryBuilders.nestedQuery(nestedPath, neQueryBuilder, ScoreMode.None);
        }
        return QueryBuilders.boolQuery().must(neQueryBuilder);
    }

    private static QueryBuilder buildLessThen(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery(fieldName).lt(fileValue);
        if (StringUtils.isNotEmpty(nestedPath)) {
            queryBuilder = QueryBuilders.nestedQuery(nestedPath, queryBuilder, ScoreMode.None);
        }
        return queryBuilder;
    }

    private static QueryBuilder buildLessEqual(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery(fieldName).lte(fileValue);
        if (StringUtils.isNotEmpty(nestedPath)) {
            queryBuilder = QueryBuilders.nestedQuery(nestedPath, queryBuilder, ScoreMode.None);
        }
        return queryBuilder;
    }

    private static QueryBuilder buildGreaterThen(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery(fieldName).gt(fileValue);
        if (StringUtils.isNotEmpty(nestedPath)) {
            queryBuilder = QueryBuilders.nestedQuery(nestedPath, queryBuilder, ScoreMode.None);
        }
        return queryBuilder;
    }

    private static QueryBuilder buildGreaterEqual(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery(fieldName).gte(fileValue);
        if (StringUtils.isNotEmpty(nestedPath)) {
            queryBuilder = QueryBuilders.nestedQuery(nestedPath, queryBuilder, ScoreMode.None);
        }
        return queryBuilder;
    }

    private static QueryBuilder buildBefore(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery(fieldName).lt(Time.getRelativeTimeString(fileValue, QueryOperators.BF));
        if (StringUtils.isNotEmpty(nestedPath)) {
            queryBuilder = QueryBuilders.nestedQuery(nestedPath, queryBuilder, ScoreMode.None);
        }
        return queryBuilder;
    }

    private static QueryBuilder buildAfter(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery(fieldName).gte(Time.getRelativeTimeString(fileValue, QueryOperators.AF));
        if (StringUtils.isNotEmpty(nestedPath)) {
            queryBuilder = QueryBuilders.nestedQuery(nestedPath, queryBuilder, ScoreMode.None);
        }
        return queryBuilder;
    }

    private static QueryBuilder buildMonthPeriod(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder startQueryBuilder = QueryBuilders.rangeQuery(fieldName).gte(MonthPeriod.getFromString(fileValue));
        QueryBuilder endQueryBuilder = QueryBuilders.rangeQuery(fieldName).lt(MonthPeriod.getToString(fileValue));
        if (StringUtils.isNotEmpty(nestedPath)) {
            startQueryBuilder = QueryBuilders.nestedQuery(nestedPath, startQueryBuilder, ScoreMode.None);
            endQueryBuilder = QueryBuilders.nestedQuery(nestedPath, endQueryBuilder, ScoreMode.None);
        }

        BoolQueryBuilder monthPeriodQueryBuilder = QueryBuilders.boolQuery();
        monthPeriodQueryBuilder.must(startQueryBuilder);
        monthPeriodQueryBuilder.must(endQueryBuilder);

        return monthPeriodQueryBuilder;
    }

    private static QueryBuilder buildNotEqual(String fieldName, String fileValue, String nestedPath) {
        QueryBuilder neQueryBuilder = QueryBuilders.termsQuery(fieldName, getValues(fileValue));
        if (StringUtils.isNotEmpty(nestedPath)) {
            neQueryBuilder = QueryBuilders.nestedQuery(nestedPath, neQueryBuilder, ScoreMode.None);
        }
        return QueryBuilders.boolQuery().mustNot(neQueryBuilder);
    }

    private static QueryBuilder buildEmpty(String fieldName, String fieldType, String fileValue, String nestedPath) {
        QueryBuilder existBuilder = QueryBuilders.existsQuery(fieldName);
        QueryBuilder blankBuilder = QueryBuilders.termQuery(fieldName, "");
        if (StringUtils.isNotEmpty(nestedPath)) {
            existBuilder = QueryBuilders.nestedQuery(nestedPath, existBuilder, ScoreMode.None);
            blankBuilder = QueryBuilders.nestedQuery(nestedPath, blankBuilder, ScoreMode.None);
        }

        BoolQueryBuilder boolQueryBuilderEmpty = QueryBuilders.boolQuery();
        boolean isEmpty = Validator.isBoolean(fileValue) && Boolean.parseBoolean(fileValue);
        if (isEmpty) {
            boolQueryBuilderEmpty.should(QueryBuilders.boolQuery().mustNot(existBuilder));
            if (Objects.equals(fieldType, KEYWORD) || Objects.equals(fieldType, TEXT))
                boolQueryBuilderEmpty.should(blankBuilder);
        } else {
            boolQueryBuilderEmpty.must(QueryBuilders.boolQuery().must(existBuilder));
            if (fieldType.equals(KEYWORD) || fieldType.equals(TEXT))
                boolQueryBuilderEmpty.mustNot(blankBuilder);
        }

        return boolQueryBuilderEmpty;
    }

    private static QueryBuilder buildLike(String fieldName, String filedValue, String nestedPath) {
        QueryBuilder likeQueryBuilder = QueryBuilders.queryStringQuery("\"" + filedValue + "\"").field(fieldName);
        if (StringUtils.isNotEmpty(nestedPath)) {
            likeQueryBuilder = QueryBuilders.nestedQuery(nestedPath, likeQueryBuilder, ScoreMode.None);
        }
        return QueryBuilders.boolQuery().must(likeQueryBuilder);
    }
}
