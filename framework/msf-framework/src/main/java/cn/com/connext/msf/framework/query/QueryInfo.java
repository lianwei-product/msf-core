package cn.com.connext.msf.framework.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.MessageFormat;
import java.util.Objects;

public class QueryInfo {

    private Pageable pageable;
    private String expression;
    private String fields;
    private int scrollSize;
    private boolean checkOperator;

    public QueryInfo() {
        checkOperator = false;
    }

    public QueryInfo(Pageable pageable, String expression) {
        this.pageable = pageable;
        this.expression = expression;
    }

    public static QueryInfo from(String expression, Object... arguments) {
        return QueryInfo.from(PageRequest.of(0, 20), expression, arguments);
    }

    public static QueryInfo from(int page, int size, String expression, Object... arguments) {
        return QueryInfo.from(PageRequest.of(page, size), expression, arguments);
    }

    public static QueryInfo from(Pageable pageable, String expression, Object... arguments) {
        String expressionFormat = null;
        if (!StringUtils.isEmpty(expression)) {
            for (int i = 0; i < arguments.length; i++) {
                Object object = arguments[i];
                if (object instanceof Number) {
                    arguments[i] = object.toString();
                }
            }
            expressionFormat = arguments.length > 0 ? MessageFormat.format(expression, arguments) : expression;
        }
        return new QueryInfo(pageable, expressionFormat);
    }

    public QueryInfo where(String field, String operator, String value) {
        expression = getExpression(field, operator, value);
        return this;
    }

    public QueryInfo and(String field, String operator, String... values) {
        if (StringUtils.isNotBlank(expression)) {
            expression += " AND " + getExpression(field, operator, values);
        } else {
            expression = getExpression(field, operator, values);
        }
        return this;
    }

    public QueryInfo or(String field, String operator, String... values) {
        if (StringUtils.isNotBlank(expression)) {
            expression += " OR " + getExpression(field, operator, values);
        } else {
            expression = getExpression(field, operator, values);
        }
        return this;
    }


    public QueryInfo fields(String fields) {
        this.fields = fields;
        return this;
    }

    public QueryInfo fields(String... fields) {
        this.fields = StringUtils.join(fields, ",");
        return this;
    }


    public QueryInfo page(int page, int size) {
        this.pageable = PageRequest.of(page, size);
        return this;
    }

    public QueryInfo page(int page, int size, Sort sort) {
        this.pageable = PageRequest.of(page, size, sort);
        return this;
    }


    public String[] getFieldArray() {
        if (StringUtils.isEmpty(fields)) return null;
        return StringUtils.split(fields, ",");
    }


    private String getExpression(String field, String operator, String... values) {
        if (values.length == 1) {
            return String.format("%s %s %s", field, operator, values[0]);
        } else if (values.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            for (String value : values) {
                if (stringBuilder.length() > 1) {
                    stringBuilder.append(" OR ");
                }
                stringBuilder.append(field).append(" ")
                        .append(operator).append(" ").append(value);
            }
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
        return "";
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public int getScrollSize() {
        return scrollSize;
    }

    public void setScrollSize(int scrollSize) {
        this.scrollSize = scrollSize;
    }

    public boolean getCheckOperator() {
        return checkOperator;
    }

    public void setCheckOperator(boolean checkOperator) {
        this.checkOperator = checkOperator;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof QueryInfo)) return false;
        final QueryInfo other = (QueryInfo) o;
        if (!other.canEqual(this)) return false;
        if (!Objects.equals(this.getPageable(), other.getPageable())) return false;
        if (!Objects.equals(this.getExpression(), other.getExpression())) return false;
        if (!Objects.equals(this.getFields(), other.getFields())) return false;
        if (this.getScrollSize() != other.getScrollSize()) return false;
        if (this.checkOperator != other.checkOperator) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof QueryInfo;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $pageable = this.getPageable();
        result = result * PRIME + ($pageable == null ? 43 : $pageable.hashCode());
        final Object $expression = this.getExpression();
        result = result * PRIME + ($expression == null ? 43 : $expression.hashCode());
        final Object $fields = this.getFields();
        result = result * PRIME + ($fields == null ? 43 : $fields.hashCode());
        result = result * PRIME + this.getScrollSize();
        result = result * PRIME + (this.checkOperator ? 79 : 97);
        return result;
    }
}
