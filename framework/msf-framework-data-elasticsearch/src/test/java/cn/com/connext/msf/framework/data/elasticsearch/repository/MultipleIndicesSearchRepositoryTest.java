package cn.com.connext.msf.framework.data.elasticsearch.repository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.framework.agg.AggregationQuery;
import cn.com.connext.msf.framework.agg.AggregationReport;
import cn.com.connext.msf.framework.data.elasticsearch.client.RestHighLevelClientBuilder;
import cn.com.connext.msf.framework.data.elasticsearch.utils.DemoMappingBuilder;
import cn.com.connext.msf.framework.data.elasticsearch.utils.DemoMemberBuilder;
import cn.com.connext.msf.framework.query.Scroll;
import cn.com.connext.msf.framework.utils.JSON;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MultipleIndicesSearchRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(MultipleIndicesSearchRepositoryTest.class);
    private final String indexName1 = "demo1";
    private final String indexName2 = "demo2";
    private final String multiIndices = "demo1,demo2";
    private final ConnextElasticSearchRepository repository;
    private final int testDataCount1 = 10;
    private final int testDataCount2 = 15;
    private final int totalTestDataCount = 25;

    public MultipleIndicesSearchRepositoryTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.INFO);
        loggerContext.getLogger("cn.com.connext").setLevel(Level.DEBUG);

        RestHighLevelClient restHighLevelClient = RestHighLevelClientBuilder.build("192.168.0.10", 9200);
        repository = new ConnextElasticSearchRepository(restHighLevelClient, RequestOptions.DEFAULT);
    }

    @Test
    public void test_save() {
        buildTestData(indexName1, 1, testDataCount1);
        buildTestData(indexName2, testDataCount1 + 1, totalTestDataCount);

        Page<ObjectNode> pageData = findPageData(multiIndices, null, null);
        logger.info(multiIndices + " page data:" + JSON.toIndentJsonString(pageData.getContent()));
        Assert.assertEquals(totalTestDataCount, pageData.getTotalElements());
    }

    @Test
    public void test_findList() {
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "memberId"));
        String[] fields = {"memberId", "realName", "province", "city"};
        QueryBuilder queryBuilder = new BoolQueryBuilder();

        List<ObjectNode> memberList = repository.findList(multiIndices, fields, pageable, queryBuilder, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(memberList));
        Assert.assertEquals(totalTestDataCount, memberList.size());
    }

    @Test
    public void test_findScroll() {
        String[] fields = {"memberId", "realName", "province", "city"};
        QueryBuilder queryBuilder = new BoolQueryBuilder();
        Scroll<ObjectNode> scrollData = repository.findScroll(multiIndices, fields, 2, Sort.by(Sort.Direction.ASC, "memberId"), queryBuilder, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(scrollData));
        List<ObjectNode> nodeList = scrollData.getContent();
        Assert.assertEquals(nodeList.size(), 2);
        Assert.assertEquals(scrollData.getTotalElements(), 25);
        Assert.assertEquals(nodeList.get(0).get("memberId").asText(), "member00001");
    }

    @Test
    public void test_findScrollById() {
        String[] fields = {"memberId", "realName", "province", "city"};
        QueryBuilder queryBuilder = new BoolQueryBuilder();
        Scroll<ObjectNode> scrollData = repository.findScroll(multiIndices, fields, 2, Sort.by(Sort.Direction.ASC, "memberId"), queryBuilder, ObjectNode.class);

        String scrollId = scrollData.getScrollId();
        scrollData = repository.findScroll(scrollId, ObjectNode.class);
        List<ObjectNode> nodeList = scrollData.getContent();
        Assert.assertEquals(nodeList.size(), 2);
        Assert.assertEquals(scrollData.getTotalElements(), 25);
        Assert.assertEquals(nodeList.get(0).get("memberId").asText(), "member00003");


        Scroll<ObjectNode> latestScrollData = null;
        while (!scrollData.isEnd()) {
            scrollData = repository.findScroll(scrollId, ObjectNode.class);
            if (!scrollData.isEnd()) {
                latestScrollData = scrollData;
            }
        }

        logger.info(JSON.toIndentJsonString(latestScrollData));
        Assert.assertEquals(latestScrollData.getTotalElements(), 25);
        Assert.assertEquals(latestScrollData.getContent().get(0).get("memberId").asText(), "member00025");
    }

    @Test
    public void test_batchProcess() {
        String[] fields = {"memberId"};
        List<String> memberIdList = Lists.newArrayList();
        repository.batchProcess(multiIndices, fields, 5, Sort.by(Sort.Direction.ASC, "memberId"), null, ObjectNode.class, (objectNode, memberIndex, memberCount) -> {
            memberIdList.add(objectNode.get("memberId").asText());
        });
        logger.info(JSON.toIndentJsonString(memberIdList));
        Assert.assertEquals(memberIdList.size(), 25);
        Assert.assertEquals(memberIdList.get(memberIdList.size() - 1), "member00025");
    }

    @Test
    public void test_agg() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        List<AggregationQuery> aggregationQueries = Lists.newArrayList();
        aggregationQueries.add(AggregationQuery.from("points_avg", "points", "avg"));
        aggregationQueries.add(AggregationQuery.from("points_sum", "points", "sum"));
        aggregationQueries.add(AggregationQuery.from("memberId_count", "memberId", "count"));
        aggregationQueries.add(AggregationQuery.from("memberId_cardinality", "memberId", "cardinality"));
        aggregationQueries.add(AggregationQuery.from("city_terms", "city", "terms"));

        AggregationReport aggregationReport = repository.aggregation(multiIndices, queryBuilder, aggregationQueries);
        logger.info(JSON.toIndentJsonString(aggregationReport));
    }

    @Test
    public void test_agg2() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("province", "上海"));

        List<AggregationQuery> aggregationQueries = Lists.newArrayList();
        aggregationQueries.add(AggregationQuery.from("province_count", "province", "count"));

        AggregationReport aggregationReport = repository.aggregation(multiIndices, queryBuilder, aggregationQueries);
        logger.info(JSON.toIndentJsonString(aggregationReport));

        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "memberId"));
        List<ObjectNode> memberList = repository.findList(multiIndices, null, pageable, queryBuilder, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(memberList));
    }


    private void buildTestData(String indexName, int start, int end) {
        repository.deleteIndex(indexName);
        repository.ensureIndex(indexName, DemoMappingBuilder.build());
        DemoMemberBuilder memberBuilder = new DemoMemberBuilder();
        for (int i = start; i <= end; i++) {
            ObjectNode member = memberBuilder.build(i);
            String memberId = member.get("memberId").asText(null);
            String content = member.toString();
            repository.save(indexName, memberId, content);
        }

        Page<ObjectNode> memberList = findPageData(indexName, null, null);
        logger.info(indexName + " page data:" + JSON.toIndentJsonString(memberList.getContent()));
        Assert.assertEquals(end - start + 1, memberList.getTotalElements());
    }

    private Page<ObjectNode> findPageData(String indexName, String[] fields, QueryBuilder queryBuilder) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "memberId"));
        return repository.findPage(indexName, fields, pageable, queryBuilder, ObjectNode.class);
    }
}