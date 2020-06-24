package cn.com.connext.msf.framework.agg;

public class AggregationQueryDate {
    private String name;
    private String field;
    private String dateHistogramInterval;
    private long dateInterval;
    private String dateFormat;
    private String from;
    private String to;

    public static AggregationQueryDate from(String name, String field, String dateHistogramInterval, long dateInterval, String dateFormat, String from, String to) {
        AggregationQueryDate aggregationQueryDate = new AggregationQueryDate();
        aggregationQueryDate.name = name;
        aggregationQueryDate.field = field;
        aggregationQueryDate.dateHistogramInterval = dateHistogramInterval;
        aggregationQueryDate.dateInterval = dateInterval;
        aggregationQueryDate.dateFormat = dateFormat;
        aggregationQueryDate.from = from;
        aggregationQueryDate.to = to;
        return aggregationQueryDate;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }

    public String getDateHistogramInterval() {
        return dateHistogramInterval;
    }

    public long getDateInterval() {
        return dateInterval;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
