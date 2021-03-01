package cn.com.connext.msf.framework.dynamic;

public enum DynamicModelFieldType {

    KEYWORD("keyword"),
    TEXT("text"),
    SHORT("short"),
    INTEGER("integer"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    DATE("date"),
    BOOLEAN("boolean"),
    GEO_POINT("geo_point"),
    NESTED("nested");

    private String elasticSearchType;

    DynamicModelFieldType(String elasticSearchType) {
        this.elasticSearchType = elasticSearchType;
    }

    public String getElasticSearchType() {
        return elasticSearchType;
    }


    public static DynamicModelFieldType fromClass(Class clazz) {
        DynamicModelFieldType result = KEYWORD;
        if (clazz.equals(String.class)) {
        } else if (clazz.equals(Integer.class)) {
            result = INTEGER;
        } else if (clazz.equals(Long.class)) {
            result = LONG;
        } else if (clazz.equals(Float.class)) {
            result = FLOAT;
        } else if (clazz.equals(Double.class)) {
            result = DOUBLE;
        } else if (clazz.equals(Boolean.class)) {
            result = BOOLEAN;
        }
        return result;
    }
}
