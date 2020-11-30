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
}
