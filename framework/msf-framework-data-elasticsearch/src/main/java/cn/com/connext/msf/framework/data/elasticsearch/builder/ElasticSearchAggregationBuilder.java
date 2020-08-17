package cn.com.connext.msf.framework.data.elasticsearch.builder;

import cn.com.connext.msf.framework.agg.AggregationQuery;
import cn.com.connext.msf.framework.agg.AggregationQueryDate;
import cn.com.connext.msf.framework.agg.AggregationQueryType;
import cn.com.connext.msf.framework.data.elasticsearch.utils.NestedPath;
import com.google.common.collect.Lists;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;

import java.time.ZoneId;
import java.util.List;

public class ElasticSearchAggregationBuilder {

    public static List<AggregationBuilder> build(List<AggregationQuery> queryList) {
        List<AggregationBuilder> builderList = Lists.newArrayList();
        queryList.forEach(query -> builderList.add(build(query)));
        return builderList;
    }

    public static AggregationBuilder build(AggregationQuery query) {
        AggregationBuilder aggregationBuilder = null;
        switch (query.getType()) {
            case AggregationQueryType.COUNT:
                aggregationBuilder = AggregationBuilders.count(query.getName()).field(query.getField());
                break;

            case AggregationQueryType.SUM:
                aggregationBuilder = AggregationBuilders.sum(query.getName()).field(query.getField());
                break;

            case AggregationQueryType.MIN:
                aggregationBuilder = AggregationBuilders.min(query.getName()).field(query.getField());
                break;

            case AggregationQueryType.MAX:
                aggregationBuilder = AggregationBuilders.max(query.getName()).field(query.getField());
                break;

            case AggregationQueryType.AVG:
                aggregationBuilder = AggregationBuilders.avg(query.getName()).field(query.getField());
                break;

            case AggregationQueryType.TERMS:
                aggregationBuilder = AggregationBuilders.terms(query.getName()).field(query.getField()).size(100).minDocCount(0);
                break;

            case AggregationQueryType.CARDINALITY:
                aggregationBuilder = AggregationBuilders.cardinality(query.getName()).field(query.getField()).precisionThreshold(40000);
                break;

            default:
                throw new RuntimeException("AggregationQueryType " + query.getType() + " is not support.");
        }


        if (query.getAggregationQueryDate() != null) {
            AggregationQueryDate aggregationQueryDate = query.getAggregationQueryDate();
            AggregationBuilder dateHistogramBuilder = AggregationBuilders
                    .dateHistogram(aggregationQueryDate.getName())
                    .field(aggregationQueryDate.getField())
                    .calendarInterval(new DateHistogramInterval(aggregationQueryDate.getDateHistogramInterval()))
                    .timeZone(ZoneId.systemDefault())
                    .format(aggregationQueryDate.getDateFormat())
                    .extendedBounds(new ExtendedBounds(aggregationQueryDate.getFrom(), aggregationQueryDate.getTo()))
                    .minDocCount(0);


            if (NestedPath.isNestedPath(aggregationQueryDate.getField())) {
                dateHistogramBuilder.subAggregation(AggregationBuilders.reverseNested(aggregationBuilder.getName()).subAggregation(aggregationBuilder));
                dateHistogramBuilder = AggregationBuilders.nested(aggregationQueryDate.getName(), NestedPath.getNestedPath(aggregationQueryDate.getField())).subAggregation(dateHistogramBuilder);
            } else {
                dateHistogramBuilder.subAggregation(aggregationBuilder);
            }

            return dateHistogramBuilder;
        } else if(NestedPath.isNestedPath(query.getField())){
            return AggregationBuilders.nested(query.getName(), NestedPath.getNestedPath(query.getField())).subAggregation(aggregationBuilder);
        }
        else {
            return aggregationBuilder;
        }
    }

}
