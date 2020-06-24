package cn.com.connext.msf.framework.agg;

public class AggregationQuery {
    private String name;
    private String field;
    private String type;
    private AggregationQueryDate aggregationQueryDate;

    public static AggregationQuery from(String name, String field, String type) {
        return from(name, field, type, null);
    }

    public static AggregationQuery from(String name, String field, String type, AggregationQueryDate aggregationQueryDate) {
        AggregationQuery query = new AggregationQuery();
        query.name = name;
        query.field = field;
        query.type = type;
        query.aggregationQueryDate = aggregationQueryDate;
        return query;
    }


    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }

    public String getType() {
        return type;
    }

    public AggregationQueryDate getAggregationQueryDate() {
        return aggregationQueryDate;
    }
}
