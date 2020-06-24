package cn.com.connext.msf.framework.query;

import cn.com.connext.msf.framework.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class QueryExpressionInfo {
    private String fieldName;
    private String operator;
    private String fieldValue;

    public static QueryExpressionInfo from(String expression) {
        QueryExpressionInfo queryExpressionInfo = new QueryExpressionInfo();
        String[] words = StringUtils.split(expression, " ", 3);
        if (words.length < 3) {
            throw new BusinessException(MessageFormat.format("Invalid query expression, expression: {0}", expression));
        }
        queryExpressionInfo.fieldName = words[0];
        queryExpressionInfo.operator = words[1];
        queryExpressionInfo.fieldValue = rebuildFieldValue(words[2]);
        return queryExpressionInfo;
    }

    private static String rebuildFieldValue(String fieldValue) {
        String result = fieldValue.replace("$L", "(").replace("$R", ")")
                .replace("$A", "AND").replace("$O", "OR");

        return result;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getOperator() {
        return operator;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
