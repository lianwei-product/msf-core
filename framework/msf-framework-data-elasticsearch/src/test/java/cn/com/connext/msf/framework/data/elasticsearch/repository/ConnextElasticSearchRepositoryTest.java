package cn.com.connext.msf.framework.data.elasticsearch.repository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.framework.agg.AggregationQuery;
import cn.com.connext.msf.framework.agg.AggregationQueryDate;
import cn.com.connext.msf.framework.agg.AggregationQueryType;
import cn.com.connext.msf.framework.agg.AggregationReport;
import cn.com.connext.msf.framework.data.elasticsearch.client.RestHighLevelClientBuilder;
import cn.com.connext.msf.framework.data.elasticsearch.utils.DemoMappingBuilder;
import cn.com.connext.msf.framework.data.elasticsearch.utils.DemoMemberBuilder;
import cn.com.connext.msf.framework.query.Scroll;
import cn.com.connext.msf.framework.utils.JSON;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexTemplatesResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConnextElasticSearchRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(ConnextElasticSearchRepositoryTest.class);
    private final String indexName = "demo";
    private final ConnextElasticSearchRepository repository;
    private final int testDataCount = 30;
    private final String templateName = "template_demo";
    private final String templatePattern = "demo_template*";

    public ConnextElasticSearchRepositoryTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.INFO);
        loggerContext.getLogger("cn.com.connext").setLevel(Level.DEBUG);

        RestHighLevelClient restHighLevelClient = RestHighLevelClientBuilder.build("192.168.0.10", 9200);
        repository = new ConnextElasticSearchRepository(restHighLevelClient, RequestOptions.DEFAULT);
    }

    @Test
    public void test01_save() {
        repository.deleteIndex(indexName);
        repository.ensureIndex(indexName, DemoMappingBuilder.build());
        DemoMemberBuilder memberBuilder = new DemoMemberBuilder();
        for (int i = 1; i <= testDataCount; i++) {
            ObjectNode member = memberBuilder.build(i);
            String memberId = member.get("memberId").asText(null);
            String content = member.toString();
            logger.info(content);
            repository.save(indexName, memberId, content);
        }
    }

    @Test
    public void test02_update() {
        UpdateRequest updateRequest = new UpdateRequest(indexName, "member00001");
        ObjectNode member = JsonNodeFactory.instance.objectNode();
        member.put("mobile", "13770655999");
        member.put("realName", "ChengHan");
        updateRequest.doc(member.toString(), XContentType.JSON);
        repository.update(updateRequest);

        String[] fields = new String[]{"memberId", "realName", "mobile"};
        member = repository.findItem(indexName, "member00001", fields, ObjectNode.class);
        Assert.assertEquals("13770655999", member.get("mobile").asText(null));
        Assert.assertEquals("ChengHan", member.get("realName").asText(null));
    }

    @Test
    public void test03_delete() {
        DemoMemberBuilder memberBuilder = new DemoMemberBuilder();
        ObjectNode member = memberBuilder.build(10000);
        String memberId = "member10000";

        repository.save(indexName, memberId, member.toString());
        boolean result = repository.findItem(indexName, memberId, null, ObjectNode.class) != null;
        Assert.assertEquals(true, result);

        repository.delete(indexName, memberId);
        result = repository.findItem(indexName, memberId, null, ObjectNode.class) != null;
        Assert.assertEquals(false, result);
    }

    @Test
    public void test02_findList() {
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "memberId"));
        String[] fields = {"memberId", "realName", "province", "city"};
        QueryBuilder queryBuilder = new BoolQueryBuilder();

        List<ObjectNode> memberList = repository.findList(indexName, fields, pageable, queryBuilder, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(memberList));
        Assert.assertEquals(testDataCount, memberList.size());
    }


    @Test
    public void test03_findPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "memberId"));
        String[] fields = {"memberId", "realName", "province", "city"};
        QueryBuilder queryBuilder = new BoolQueryBuilder();

        Page<ObjectNode> memberPage = repository.findPage(indexName, fields, pageable, queryBuilder, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(memberPage));
        Assert.assertEquals(testDataCount, memberPage.getTotalElements());
    }

    @Test
    public void test_bulkFindItem() {
        List<ObjectNode> list = repository.bulkFindItem(indexName, Arrays.asList("member00001", "member00002", "member00003"), ObjectNode.class);
        logger.info(JSON.toIndentJsonString(list));
        Assert.assertEquals(list.size(), 3);
    }

    @Test
    public void test_findScroll() {
        String[] fields = {"memberId", "realName", "province", "city"};
        QueryBuilder queryBuilder = new BoolQueryBuilder();
        Scroll<ObjectNode> scrollData = repository.findScroll(indexName, fields, 2, Sort.by(Sort.Direction.ASC, "memberId"), queryBuilder, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(scrollData));
        List<ObjectNode> nodeList = scrollData.getContent();
        Assert.assertEquals(nodeList.size(), 2);
        Assert.assertEquals(scrollData.getTotalElements(), 30);
        Assert.assertEquals(nodeList.get(0).get("memberId").asText(), "member00001");

    }

    @Test
    public void test_findScrollById() {
        String[] fields = {"memberId", "realName", "province", "city"};
        QueryBuilder queryBuilder = new BoolQueryBuilder();
        Scroll<ObjectNode> scrollData = repository.findScroll(indexName, fields, 2, Sort.by(Sort.Direction.ASC, "memberId"), queryBuilder, ObjectNode.class);


        String scrollId = scrollData.getScrollId();
        scrollData = repository.findScroll(scrollId, ObjectNode.class);

        logger.info(JSON.toIndentJsonString(scrollData));
        List<ObjectNode> nodeList = scrollData.getContent();
        Assert.assertEquals(nodeList.size(), 2);
        Assert.assertEquals(scrollData.getTotalElements(), 30);
        Assert.assertEquals(nodeList.get(0).get("memberId").asText(), "member00003");
    }

    @Test
    public void test_batchProcess() {
        String[] fields = {"memberId"};
        repository.batchProcess(indexName, fields, 5, Sort.by(Sort.Direction.ASC, "memberId"), null, ObjectNode.class, (objectNode, memberIndex, memberCount) -> {
            logger.info(JSON.toIndentJsonString(objectNode));
        });
    }

    @Test
    public void test_agg() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("province", "上海"));

        List<AggregationQuery> aggregationQueries = Lists.newArrayList();
        aggregationQueries.add(AggregationQuery.from("points_avg", "points", "avg"));
        aggregationQueries.add(AggregationQuery.from("points_sum", "points", "sum"));
        aggregationQueries.add(AggregationQuery.from("points_count", "points", "count"));
        aggregationQueries.add(AggregationQuery.from("points_cardinality", "points", "cardinality"));
        aggregationQueries.add(AggregationQuery.from("city_terms", "city", "terms"));

        AggregationReport aggregationReport = repository.aggregation(indexName, queryBuilder, aggregationQueries);
        logger.info(JSON.toIndentJsonString(aggregationReport));
    }

    @Test
    public void test_AddTag() {
        UpdateRequest updateRequest = new UpdateRequest(indexName, "member00005");

        Map<String, Object> params = Maps.newHashMap();
        params.put("new_tag0", "female");

        Script script = new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG, "if(!ctx._source.cdp_tag.contains(params.new_tag0)){ctx._source.cdp_tag.add(params.new_tag0)}", params);
        updateRequest.script(script);
        repository.update(updateRequest);

        params = Maps.newHashMap();
        params.put("new_tag0", "latest");
        script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, "if(!ctx._source.cdp_tag.contains(params.new_tag0)){ctx._source.cdp_tag.add(params.new_tag0)}", params);
        updateRequest.script(script);
        repository.update(updateRequest);
    }

    @Test
    public void test_CleanMultiTag() {
        List<UpdateRequest> updateRequestList = Lists.newArrayList();
        UpdateRequest updateRequest = new UpdateRequest(indexName, "member00005");
        Map<String, Object> params = Maps.newHashMap();
        params.put("new_tag0", "female");
        Script script = new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG, "if(ctx._source.cdp_tag.contains(params.new_tag0)){ctx._source.cdp_tag.remove(ctx._source.cdp_tag.indexOf(params.new_tag0))}", params);
        updateRequest.script(script);
        updateRequestList.add(updateRequest);

        updateRequest = new UpdateRequest(indexName, "member00005");
        params = Maps.newHashMap();
        params.put("new_tag0", "latest");
        script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, "if(ctx._source.cdp_tag.contains(params.new_tag0)){ctx._source.cdp_tag.remove(ctx._source.cdp_tag.indexOf(params.new_tag0))}", params);
        updateRequest.script(script);
        updateRequestList.add(updateRequest);

        repository.bulkUpdate(updateRequestList);
    }

    @Test
    public void test_bulkUpdate() {
        List<UpdateRequest> updateRequestList = Lists.newArrayList();

        UpdateRequest updateRequest = new UpdateRequest(indexName, "member00003");
        ObjectNode member = JsonNodeFactory.instance.objectNode();
        member.put("mobile", "13770000003");
        member.put("realName", "test00003");
        updateRequest.doc(member.toString(), XContentType.JSON);
        updateRequestList.add(updateRequest);

        updateRequest = new UpdateRequest(indexName, "member00004");
        member = JsonNodeFactory.instance.objectNode();
        member.put("mobile", "13770000004");
        member.put("realName", "test00004");
        updateRequest.doc(member.toString(), XContentType.JSON);
        updateRequestList.add(updateRequest);

        repository.bulkUpdate(updateRequestList);

        String[] fields = new String[]{"memberId", "realName", "mobile"};
        member = repository.findItem(indexName, "member00003", fields, ObjectNode.class);
        Assert.assertEquals("13770000003", member.get("mobile").asText(null));
        Assert.assertEquals("test00003", member.get("realName").asText(null));

        member = repository.findItem(indexName, "member00004", fields, ObjectNode.class);
        Assert.assertEquals("13770000004", member.get("mobile").asText(null));
        Assert.assertEquals("test00004", member.get("realName").asText(null));
    }

    @Test
    public void test_deleteByQuery() {
        DemoMemberBuilder memberBuilder = new DemoMemberBuilder();
        ObjectNode member = memberBuilder.build(10001);
        String memberId = "member10001";

        repository.save(indexName, memberId, member.toString());
        boolean result = repository.findItem(indexName, memberId, null, ObjectNode.class) != null;
        Assert.assertEquals(true, result);

        member = memberBuilder.build(10002);
        memberId = "member10002";

        repository.save(indexName, memberId, member.toString());
        result = repository.findItem(indexName, memberId, null, ObjectNode.class) != null;
        Assert.assertEquals(true, result);


        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.should(QueryBuilders.termQuery("memberId", "member10001"));
        queryBuilder.should(QueryBuilders.termQuery("memberId", "member10002"));

        repository.deleteByQuery(indexName, queryBuilder);
        result = repository.findItem(indexName, "member10001", null, ObjectNode.class) != null;
        Assert.assertEquals(false, result);

        result = repository.findItem(indexName, "member10002", null, ObjectNode.class) != null;
        Assert.assertEquals(false, result);
    }

    @Test
    public void test_bulkDelete() {
        DemoMemberBuilder memberBuilder = new DemoMemberBuilder();
        ObjectNode member = memberBuilder.build(10003);
        String memberId = "member10003";

        repository.save(indexName, memberId, member.toString());
        boolean result = repository.findItem(indexName, memberId, null, ObjectNode.class) != null;
        Assert.assertEquals(true, result);

        member = memberBuilder.build(10004);
        memberId = "member10004";

        repository.save(indexName, memberId, member.toString());
        result = repository.findItem(indexName, memberId, null, ObjectNode.class) != null;
        Assert.assertEquals(true, result);

        List<DeleteRequest> deleteRequestList = Lists.newArrayList();
        deleteRequestList.add(new DeleteRequest(indexName, "member10003"));
        deleteRequestList.add(new DeleteRequest(indexName, "member10004"));

        repository.bulkDelete(deleteRequestList);
        result = repository.findItem(indexName, "member10003", null, ObjectNode.class) != null;
        Assert.assertEquals(false, result);

        result = repository.findItem(indexName, "member10004", null, ObjectNode.class) != null;
        Assert.assertEquals(false, result);
    }

//    @Test
    public void test_agg2() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        AggregationQueryDate date = AggregationQueryDate.from("countByDay", "wx_subscribeTime", "1d", 1, "yyyy-MM-dd", "2018-01-01", "2020-01-01");
        AggregationQuery query = AggregationQuery.from("subscribeCount", "wx_openId", AggregationQueryType.CARDINALITY, date);

        AggregationReport aggregationReport = repository.aggregation("cdp_going_mary_scrm_gh_a1832ddc7ffe_fans", queryBuilder, Lists.newArrayList(query));
        logger.info(JSON.toIndentJsonString(aggregationReport));
    }

    @Test
    public void test_put_index_template() {
        repository.putTemplate(templateName, Arrays.asList(templatePattern), DemoMappingBuilder.build());
    }

    @Test
    public void test_exists_index_template() {
        boolean isTemplateExists = repository.existTemplate(templateName);
        Assert.assertEquals(true, isTemplateExists);
    }

    @Test
    public void test_get_index_template() {
        GetIndexTemplatesResponse response = repository.getTemplate(templateName);
        logger.info(response.toString());
    }

    @Test
    public void test_delete_index_template() {
        repository.deleteTemplate(templateName);
        boolean isTemplateExists = repository.existTemplate(templateName);
        Assert.assertEquals(false, isTemplateExists);
    }

    @Test
    public void test_provide_data_by_template() {
        String indexName = "demo_template-2020.01";
        DemoMemberBuilder memberBuilder = new DemoMemberBuilder();
        ObjectNode member = memberBuilder.build(1);
        String memberId = member.get("memberId").asText(null);
        String content = member.toString();
        logger.info(content);
        repository.save(indexName, memberId, content);

        ObjectNode memberNode = repository.findItem(indexName, "member00001", null, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(memberNode));
        Assert.assertEquals(memberNode.get("memberId").asText(), "member00001");
    }

    @Test
    public void test_put_index_template2() {
        repository.putTemplate(templateName, Arrays.asList(templatePattern), DemoMappingBuilder.build2());

        String indexName = "demo_template-2020.01";
        DemoMemberBuilder memberBuilder = new DemoMemberBuilder();
        ObjectNode member = memberBuilder.build(2);
        String memberId = member.get("memberId").asText(null);
        String content = member.toString();
        logger.info(content);
        repository.save(indexName, memberId, content);

        ObjectNode memberNode = repository.findItem(indexName, "member00002", null, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(memberNode));
        Assert.assertEquals(memberNode.get("memberId").asText(), "member00002");
    }

    @Test
    public void test_put_index_template3() {
        repository.putTemplate(templateName, Arrays.asList(templatePattern), DemoMappingBuilder.build2());

        String indexName = "demo_template-2020.02";
        DemoMemberBuilder memberBuilder = new DemoMemberBuilder();
        ObjectNode member = memberBuilder.build(1);
        String memberId = member.get("memberId").asText(null);
        String content = member.toString();
        logger.info(content);
        repository.save(indexName, memberId, content);

        ObjectNode memberNode = repository.findItem(indexName, "member00001", null, ObjectNode.class);
        logger.info(JSON.toIndentJsonString(memberNode));
        Assert.assertEquals(memberNode.get("memberId").asText(), "member00001");
    }
}