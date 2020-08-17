package cn.com.connext.msf.framework.data.elasticsearch.builder;//package cn.com.connext.msf.data.elasticsearch.agg;
//
//import cn.com.connext.msf.data.elasticsearch.ConnextElasticSearchRepository;
//import cn.com.connext.msf.framework.agg.AggregationQuery;
//import cn.com.connext.msf.framework.agg.AggregationQueryDate;
//import cn.com.connext.msf.framework.agg.AggregationQueryType;
//import cn.com.connext.msf.framework.agg.AggregationReport;
//import cn.com.connext.msf.framework.utils.JSON;
//import com.google.common.collect.Lists;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.AggregationBuilder;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
//
//import java.util.List;
//
//public class AggregationQueryTest {
//
//    private final Logger logger = LoggerFactory.getLogger(AggregationQueryTest.class);
//    private final ConnextElasticSearchRepository connextElasticSearchRepository;
//    private final TransportClient transportClient;
//
//    private String index = "cdp_connext_brand_a_order";
//    private String type = "cdp_doc";
//
//    public AggregationQueryTest() throws Exception {
//        TransportClientFactoryBean transportClientFactoryBean = new TransportClientFactoryBean();
//        transportClientFactoryBean.setClusterName("saas");
//        transportClientFactoryBean.setClusterNodes("106.14.192.217:9300");
//        transportClientFactoryBean.setClientTransportSniff(false);
//        transportClientFactoryBean.afterPropertiesSet();
//        transportClient = transportClientFactoryBean.getObject();
//        connextElasticSearchRepository = new ConnextElasticSearchRepository(transportClient);
//    }
//
//    @Test
//    public void test() {
//
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        queryBuilder.filter(QueryBuilders.rangeQuery("orderTime").gte("2017-07-01"));
//        queryBuilder.filter(QueryBuilders.rangeQuery("orderTime").lt("2017-07-12"));
//
//        String from = "2017-07-01";
//        String to = "2017-07-11";
//
//
//        AggregationQueryDate date01 = AggregationQueryDate.from("countByDay", "orderTime", "1d", 1, "yyyy-MM-dd", from, to);
//        AggregationQuery query01 = AggregationQuery.from("count", "cdp_id", AggregationQueryType.COUNT, date01);
//
//        AggregationQueryDate date02 = AggregationQueryDate.from("amountSumByDay", "orderTime", "1d", 1, "yyyy-MM-dd", from, to);
//        AggregationQuery query02 = AggregationQuery.from("sum", "amount", AggregationQueryType.SUM, date02);
//
//        AggregationQuery query03 = AggregationQuery.from("totalCount", "cdp_id", AggregationQueryType.COUNT);
//        AggregationQuery query04 = AggregationQuery.from("totalAmount", "amount", AggregationQueryType.SUM);
//
//        AggregationQueryDate date05 = AggregationQueryDate.from("personCountByDay", "orderTime", "1d", 1, "yyyy-MM-dd", from, to);
//        AggregationQuery query05 = AggregationQuery.from("personCount", "jdMemberId", AggregationQueryType.CARDINALITY, date05);
//
//        List<AggregationQuery> aggregationQueryList = Lists.newArrayList(query01, query02, query03, query04, query05);
//        AggregationReport report = connextElasticSearchRepository.aggregation(index, type, queryBuilder, aggregationQueryList);
//        logger.info(JSON.toIndentJsonString(report));
//    }
//
//    @Test
//    public void test01() {
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        AggregationQuery query = AggregationQuery.from("region", "wx_province", AggregationQueryType.TERMS);
//
//        List<AggregationQuery> aggregationQueryList = Lists.newArrayList(query);
//        AggregationReport report = connextElasticSearchRepository.aggregation("cdp_scrm_gh_75ea1a888699_fans", type, queryBuilder, aggregationQueryList);
//        logger.info(JSON.toIndentJsonString(report));
//    }
//
//    @Test
//    public void test02() {
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        AggregationQuery query = AggregationQuery.from("rule", "items.ruleId", AggregationQueryType.TERMS);
//
//        List<AggregationQuery> aggregationQueryList = Lists.newArrayList(query);
//        AggregationReport report = connextElasticSearchRepository.aggregation("cdp_connext_hh_customer_point", type, queryBuilder, aggregationQueryList);
//        logger.info(JSON.toIndentJsonString(report));
//    }
//
//    @Test
//    public void test03() {
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        AggregationQuery query = AggregationQuery.from("totalPoints", "items.points", AggregationQueryType.SUM);
//
//        List<AggregationQuery> aggregationQueryList = Lists.newArrayList(query);
//        AggregationReport report = connextElasticSearchRepository.aggregation("cdp_connext_hh_customer_point", type, queryBuilder, aggregationQueryList);
//        logger.info(JSON.toIndentJsonString(report));
//    }
//
//    @Test
//    public void test04() {
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//
//        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("nestedAgg", "items");
//        AggregationBuilder ruleIdAgg = AggregationBuilders.terms("ruleId").field("items.ruleId");
//        nestedAggregationBuilder.subAggregation(ruleIdAgg);
//
//        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch("cdp_connext_hh_customer_point").setTypes(type)
//                .setQuery(queryBuilder).setFrom(0).setSize(0);
//        searchRequestBuilder.addAggregation(nestedAggregationBuilder);
//
//        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
//        Aggregations aggregations = searchResponse.getAggregations();
//
//
//    }
//}