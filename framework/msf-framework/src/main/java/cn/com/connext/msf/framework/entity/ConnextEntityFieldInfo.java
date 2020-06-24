package cn.com.connext.msf.framework.entity;

import cn.com.connext.msf.framework.annotation.QueryVerb;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class ConnextEntityFieldInfo {
    private String name;
    private String type;
    private boolean allowSort;
    private Set<String> operators;

    public ConnextEntityFieldInfo() {
        operators = new HashSet<>();
    }

    public static ConnextEntityFieldInfo from(String name, String type, boolean allowSort, Set<String> operators) {
        ConnextEntityFieldInfo fieldInfo = new ConnextEntityFieldInfo();
        fieldInfo.name = name;
        fieldInfo.type = type;
        fieldInfo.allowSort = allowSort;
        fieldInfo.operators = operators;
        return fieldInfo;
    }

    public static ConnextEntityFieldInfo from(Field field, boolean sqlSupport, boolean nosqlSupport) {
        ConnextEntityFieldInfo fieldInfo = new ConnextEntityFieldInfo();
        fieldInfo.setName(field.getName());

        if (field.getType().isEnum()) {
            fieldInfo.setType("java.lang.String");
        } else {
            fieldInfo.setType(field.getType().getName());
        }

        if (sqlSupport) {
            javax.persistence.Id id = field.getAnnotation(javax.persistence.Id.class);
            if (id != null) {
                fieldInfo.addOperator("eq");
            }
        }

        if (nosqlSupport) {
            org.springframework.data.annotation.Id id = field.getAnnotation(org.springframework.data.annotation.Id.class);
            if (id != null) {
                fieldInfo.addOperator("eq");
            }
        }


        QueryVerb queryVerb = field.getAnnotation(QueryVerb.class);
        if (queryVerb != null) {
            fieldInfo.addOperator(queryVerb.operator());
            fieldInfo.allowSort = queryVerb.allowSort();
        }

        fieldInfo.checkOperators();

        return fieldInfo;
    }

    public static ConnextEntityFieldInfo from(String parentName, Field field, boolean sqlSupport, boolean nosqlSupport) {
        ConnextEntityFieldInfo fieldInfo = new ConnextEntityFieldInfo();
        if(StringUtils.isNotBlank(parentName)){
            fieldInfo.setName(parentName + "." + field.getName());
        }else{
            fieldInfo.setName(field.getName());
        }

        if (field.getType().isEnum()) {
            fieldInfo.setType("java.lang.String");
        } else {
            fieldInfo.setType(field.getType().getName());
        }

        if (sqlSupport) {
            javax.persistence.Id id = field.getAnnotation(javax.persistence.Id.class);
            if (id != null) {
                fieldInfo.addOperator("eq");
            }
        }

        if (nosqlSupport) {
            org.springframework.data.annotation.Id id = field.getAnnotation(org.springframework.data.annotation.Id.class);
            if (id != null) {
                fieldInfo.addOperator("eq");
            }
        }


        QueryVerb queryVerb = field.getAnnotation(QueryVerb.class);
        if (queryVerb != null) {
            fieldInfo.addOperator(queryVerb.operator());
            fieldInfo.allowSort = queryVerb.allowSort();
        }

        fieldInfo.checkOperators();

        return fieldInfo;
    }

    private void addOperator(String operatorString) {
        if (StringUtils.isEmpty(operatorString)) {
            return;
        }

        String[] operatorStringArray = StringUtils.trimToEmpty(operatorString).toLowerCase().split(",");
        for (String operator : operatorStringArray) {
            String formatedOperator = formatOperator(operator);
            operators.add(formatedOperator);
        }
    }

    private String formatOperator(String operator) {
        switch (operator) {
            case "eq":
            case "equal":
                return "eq";

            case "ne":
            case "notequal":
                return "ne";

            case "lt":
            case "lessthen":
                return "lt";

            case "le":
            case "lessequal":
                return "le";

            case "gt":
            case "greaterthen":
                return "gt";

            case "ge":
            case "greaterequal":
                return "ge";

            case "like":
                return "like";
        }
        throw new RuntimeException("Unsupport operator: " + operator);
    }

    private void checkOperators() {
        switch (type) {
            case "java.lang.String":
                if (operators.contains("lt") || operators.contains("le")
                        || operators.contains("gt") || operators.contains("ge")) {
                    throw new RuntimeException("String type field does not support lt, le, gt, ge operator.");
                }
                break;

            case "java.lang.Integer":
            case "int":
            case "java.lang.Long":
            case "long":
            case "java.lang.Double":
            case "double":
            case "java.lang.Float":
            case "float":
                if (operators.contains("like")) {
                    throw new RuntimeException("Number type field does not support like operator.");
                }
                break;

            case "java.util.Date":
            case "java.sql.Date":
                if (operators.contains("like")) {
                    throw new RuntimeException("Date type field does not support like operator.");
                }
                break;

            case "java.lang.Boolean":
            case "boolean":
                if (operators.contains("like")
                        || operators.contains("lt") || operators.contains("le")
                        || operators.contains("gt") || operators.contains("ge")) {
                    throw new RuntimeException("bool type field does not support like, lt, le, gt, ge operator.");
                }
                break;

        }
    }

    // region getter&setter


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAllowSort() {
        return allowSort;
    }

    public void setAllowSort(boolean allowSort) {
        this.allowSort = allowSort;
    }

    public Set<String> getOperators() {
        return operators;
    }

    public void setOperators(Set<String> operators) {
        this.operators = operators;
    }

    // endregion
}
