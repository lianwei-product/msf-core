package cn.com.connext.msf.framework.data.elasticsearch.repository;

import cn.com.connext.msf.framework.agg.AggregationQuery;
import cn.com.connext.msf.framework.agg.AggregationQueryType;
import cn.com.connext.msf.framework.agg.AggregationReport;
import cn.com.connext.msf.framework.agg.AggregationTable;
import cn.com.connext.msf.framework.data.elasticsearch.builder.ElasticSearchAggregationBuilder;
import cn.com.connext.msf.framework.data.elasticsearch.config.ConnextElasticSearchFeature;
import cn.com.connext.msf.framework.data.elasticsearch.utils.NestedPath;
import cn.com.connext.msf.framework.query.Scroll;
import cn.com.connext.msf.framework.utils.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.template.delete.DeleteIndexTemplateRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedReverseNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.ParsedSingleValueNumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class ConnextElasticSearchRepository {

    private final Logger logger = LoggerFactory.getLogger(ConnextElasticSearchRepository.class);

    private final RestHighLevelClient client;
    private final RequestOptions options;

    public ConnextElasticSearchRepository(RestHighLevelClient client, RequestOptions options) {
        this.client = client;
        this.options = options;
    }

    public void save(String index, String id, String source) {
        IndexRequest request = new IndexRequest(index)
                .id(id)
                .source(source, XContentType.JSON)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        ConnextElasticSearchFeature.prepareRequestType(request);
        try {
            client.index(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Save document to elasticSearch error.", e);
        }
    }

    public void save(IndexRequest request) {
        ConnextElasticSearchFeature.prepareRequestType(request);
        try {
            client.index(request, options);
        } catch (IOException e) {
            throw new RuntimeException("Create document to elasticSearch error.", e);
        }
    }

    public void update(UpdateRequest request) {
        ConnextElasticSearchFeature.prepareRequestType(request);
        try {
            client.update(request, options);
        } catch (IOException e) {
            throw new RuntimeException("Update document to elasticSearch error.", e);
        }
    }

    public void delete(String index, String id) {
        DeleteRequest request = new DeleteRequest(index, id).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        ConnextElasticSearchFeature.prepareRequestType(request);
        try {
            client.delete(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Delete document from elasticSearch error.", e);
        }
    }

    public void deleteByQuery(String index, QueryBuilder queryBuilder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setConflicts("proceed");
        request.setQuery(queryBuilder);
        request.setRefresh(true);

        try {
            client.deleteByQuery(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Delete document by query from elasticSearch error.", e);
        }
    }

    public void bulkCreate(List<IndexRequest> createRequestList) {
        if (createRequestList == null || createRequestList.size() == 0)
            return;

        BulkRequest bulkRequest = new BulkRequest();
        createRequestList.forEach(bulkRequest::add);
        try {
            client.bulk(bulkRequest, options);
        } catch (Exception e) {
            throw new RuntimeException("BulkCreate document to elasticSearch error.", e);
        }
    }

    public void bulkUpdate(List<UpdateRequest> updateRequestList) {
        if (updateRequestList == null || updateRequestList.size() == 0)
            return;

        BulkRequest bulkRequest = new BulkRequest();
        updateRequestList.forEach(bulkRequest::add);
        try {
            client.bulk(bulkRequest, options);
        } catch (Exception e) {
            throw new RuntimeException("BulkUpdate document to elasticSearch error.", e);
        }
    }

    public void bulkDelete(List<DeleteRequest> deleteRequestList) {
        if (deleteRequestList == null || deleteRequestList.size() == 0)
            return;

        BulkRequest bulkRequest = new BulkRequest();
        deleteRequestList.forEach(bulkRequest::add);
        try {
            client.bulk(bulkRequest, options);
        } catch (Exception e) {
            throw new RuntimeException("BulkDelete document to elasticSearch error.", e);
        }
    }

    public <T> T findItem(String index, String id, String[] fields, Class<T> objectClass) {
        GetRequest request = new GetRequest(index, id)
                .fetchSourceContext(new FetchSourceContext(true, fields, null));

        ConnextElasticSearchFeature.prepareRequestType(request);
        GetResponse getResponse;
        try {
            getResponse = client.get(request, options);
        } catch (IOException e) {
            throw new RuntimeException("Find document from elasticSearch error.", e);
        }

        if (getResponse.isExists()) {
            return JSON.parseObject(getResponse.getSourceAsString(), objectClass);
        } else {
            return null;
        }
    }

    public <T> List<T> findList(String index, String[] fields, Pageable pageable, QueryBuilder queryBuilder, Class<T> objectClass) {
        SearchResponse searchResponse = find(index, fields, pageable, queryBuilder);
        SearchHits hits = searchResponse.getHits();
        return parseHits(hits, objectClass);
    }

    public <T> Page<T> findPage(String index, String[] fields, Pageable pageable, QueryBuilder queryBuilder, Class<T> objectClass) {
        SearchResponse searchResponse = find(index, fields, pageable, queryBuilder);
        SearchHits hits = searchResponse.getHits();
        List<T> content = parseHits(hits, objectClass);
        return new PageImpl<T>(content, pageable, hits.getTotalHits().value);
    }

    public void ensureIndex(String indexName, String mapping) {
        ensureIndex(indexName, mapping, buildSettings());
    }

    public void ensureIndex(String indexName, String mapping, Settings settings) {
        if (!existIndex(indexName)) {
            createIndex(indexName, mapping, settings);
        } else {
            modifyIndex(indexName, mapping);
        }
    }

    public void createIndex(String indexName, String mapping) {
        createIndex(indexName, mapping, buildSettings());
    }

    public void createIndex(String indexName, String mapping, Settings settings) {
        CreateIndexRequest request = new CreateIndexRequest(indexName)
                .mapping(mapping, XContentType.JSON)
                .settings(settings);
        try {
            client.indices().create(request, options);
        } catch (Exception e) {
            if (e.getMessage().startsWith("Unable to parse response body for Response")) {
                return;
            }
            throw new RuntimeException("Create index mapping error.", e);
        }
    }

    public void modifyIndex(String indexName, String mapping) {
        PutMappingRequest request = new PutMappingRequest(indexName)
                .source(mapping, XContentType.JSON);
        try {
            client.indices().putMapping(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Modify index mapping error.", e);
        }
    }

    public void deleteIndex(String indexName) {
        if (!existIndex(indexName))
            return;

        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        try {
            client.indices().delete(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Delete index mapping error.", e);
        }
    }

    public boolean existIndex(String indexName) {
        GetIndexRequest request = new GetIndexRequest(indexName);
        try {
            return client.indices().exists(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Check index name exist error.", e);
        }
    }

    protected SearchResponse find(String index, String[] fields, Pageable pageable, QueryBuilder queryBuilder) {
        return search(index, fields, (int) pageable.getOffset(), pageable.getPageSize(), pageable.getSort(), queryBuilder, null);
    }

    protected SearchResponse search(String index, QueryBuilder queryBuilder, List<AggregationBuilder> aggregationBuilders) {
        return search(index, null, 0, 0, null, queryBuilder, aggregationBuilders);
    }

    protected SearchResponse search(String index, String[] fields, int offset, int size, Sort sort, QueryBuilder queryBuilder, List<AggregationBuilder> aggregationBuilders) {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.fetchSource(fields, null);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.from(offset);
        sourceBuilder.size(size);

        if (ConnextElasticSearchFeature.getPrimaryVersion() == 7) {
            sourceBuilder.trackTotalHits(true);
        }

        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        if (sort != null) {
            for (Sort.Order order : sort) {
                sourceBuilder.sort(order.getProperty(), order.getDirection().isDescending() ? SortOrder.DESC : SortOrder.ASC);
            }
        }

        if (aggregationBuilders != null) {
            aggregationBuilders.forEach(sourceBuilder::aggregation);
        }


        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.source(sourceBuilder);

        try {
            return client.search(searchRequest, options);
        } catch (IOException e) {
            throw new RuntimeException("search document from elasticSearch error.", e);
        }
    }

    protected <T> List<T> parseHits(SearchHits hits, Class<T> objectClass) {
        List<T> list = Lists.newArrayList();
        for (SearchHit searchHit : hits) {
            try {
                T t = JSON.parseObject(searchHit.getSourceAsString(), objectClass);
                if (t != null) {
                    list.add(t);
                }
            } catch (Exception e) {
                logger.error("Parse searchHit error: " + searchHit.getSourceAsString());
                throw new RuntimeException(e);
            }

        }
        return list;
    }

    private Settings buildSettings() {
        return buildSettings(6);
    }

    private Settings buildSettings(int shardNumber) {
        return Settings.builder().put("number_of_shards", shardNumber).put("number_of_replicas", 1).build();
    }

    public <T> List<T> bulkFindItem(String index, List<String> idList, Class<T> objectClass) {
        MultiGetRequest request = new MultiGetRequest();
        for (String id : idList) {
            request.add(new MultiGetRequest.Item(index, id));
        }

        MultiGetResponse response;
        try {
            response = client.mget(request, options);
        } catch (IOException e) {
            throw new RuntimeException("Bulk find document from elasticSearch error.", e);
        }

        List<T> list = Lists.newArrayList();
        for (int i = 0; i < response.getResponses().length; i++) {
            MultiGetItemResponse itemResponse = response.getResponses()[i];
            if (itemResponse.getResponse().isExists()) {
                list.add(JSON.parseObject(itemResponse.getResponse().getSourceAsString(), objectClass));
            }
        }
        return list;
    }

    public <T> Scroll<T> findScroll(String index, String[] fields, int scrollSize, Sort sort, QueryBuilder queryBuilder, Class<T> objectClass) {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        if (sort != null) {
            for (Sort.Order order : sort) {
                sourceBuilder.sort(order.getProperty(), order.getDirection().isDescending() ? SortOrder.DESC : SortOrder.ASC);
            }
        }

        sourceBuilder.fetchSource(fields, null);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.size(scrollSize);
        request.source(sourceBuilder);
        request.scroll(TimeValue.timeValueSeconds(60));

        SearchResponse searchResponse;
        try {
            searchResponse = client.search(request, options);
        } catch (IOException e) {
            throw new RuntimeException("Find scroll document from elasticSearch error.", e);
        }

        String scrollId = searchResponse.getScrollId();
        SearchHits hits = searchResponse.getHits();
        List<T> content = parseHits(hits, objectClass);
        return new Scroll<>(scrollId, new Date(new Date().getTime() + 60000), hits.getTotalHits().value, content);
    }

    public <T> Scroll<T> findScroll(String scrollId, Class<T> objectClass) {
        SearchScrollRequest request = new SearchScrollRequest(scrollId);
        request.scroll(TimeValue.timeValueSeconds(60));
        SearchResponse searchResponse;
        try {
            searchResponse = client.scroll(request, options);
        } catch (IOException e) {
            throw new RuntimeException("Find scroll document from elasticSearch by scrollId error.", e);
        }

        SearchHits hits = searchResponse.getHits();
        List<T> content = parseHits(hits, objectClass);
        return new Scroll<>(scrollId, new Date(new Date().getTime() + 60000), hits.getTotalHits().value, content);
    }

    public <T> long batchProcess(String index, String[] fields, int scrollSize, QueryBuilder queryBuilder, Class<T> objectClass, DataProcessor<T> dataProcessor) {
        return batchProcess(index, fields, scrollSize, null, queryBuilder, objectClass, dataProcessor);
    }

    public <T> long batchProcess(String index, String[] fields, int scrollSize, Sort sort, QueryBuilder queryBuilder, Class<T> objectClass, DataProcessor<T> dataProcessor) {
        Scroll<T> scroll = findScroll(index, fields, scrollSize, sort, queryBuilder, objectClass);
        long itemIndex = 0;
        while (!scroll.isEnd()) {
            for (T t : scroll.getContent()) {
                itemIndex++;
                dataProcessor.process(t, itemIndex, scroll.getTotalElements());
            }
            scroll = findScroll(scroll.getScrollId(), objectClass);
        }
        return scroll.getTotalElements();
    }

    public <T> long batchProcess(String index, String[] fields, int scrollSize, QueryBuilder queryBuilder, Class<T> objectClass, DataListProcessor<T> dataListProcessor) {
        return batchProcess(index, fields, scrollSize, null, queryBuilder, objectClass, dataListProcessor);
    }

    public <T> long batchProcess(String index, String[] fields, int scrollSize, Sort sort, QueryBuilder queryBuilder, Class<T> objectClass, DataListProcessor<T> dataListProcessor) {
        Scroll<T> scroll = findScroll(index, fields, scrollSize, sort, queryBuilder, objectClass);
        long itemIndex = 1;
        while (!scroll.isEnd()) {
            long size = scroll.getContent().size();
            long from = itemIndex;
            long to = from + size - 1;
            dataListProcessor.process(scroll.getContent(), from, to, scroll.getTotalElements());
            itemIndex = itemIndex + size;
            scroll = findScroll(scroll.getScrollId(), objectClass);
        }
        return scroll.getTotalElements();
    }

    public <T> long batchManagedProcess(String index, String[] fields, int scrollSize, QueryBuilder queryBuilder, Class<T> objectClass, ManagedDataProcessor<T> managedDataProcessor) {
        return batchManagedProcess(index, fields, scrollSize, null, queryBuilder, objectClass, managedDataProcessor);
    }

    public <T> long batchManagedProcess(String index, String[] fields, int scrollSize, Sort sort, QueryBuilder queryBuilder, Class<T> objectClass, ManagedDataProcessor<T> managedDataProcessor) {
        Scroll<T> scroll = findScroll(index, fields, scrollSize, sort, queryBuilder, objectClass);
        long itemIndex = 0;
        boolean isContinue = true;
        while (isContinue && !scroll.isEnd()) {
            for (T t : scroll.getContent()) {
                itemIndex++;
                isContinue = managedDataProcessor.process(t, itemIndex, scroll.getTotalElements());
                if (!isContinue) {
                    return scroll.getTotalElements();
                }
            }
            scroll = findScroll(scroll.getScrollId(), objectClass);
        }
        return scroll.getTotalElements();
    }

    public <T> long batchManagedProcess(String index, String[] fields, int scrollSize, QueryBuilder queryBuilder, Class<T> objectClass, ManagedDataListProcessor<T> managedDataListProcessor) {
        return batchManagedProcess(index, fields, scrollSize, null, queryBuilder, objectClass, managedDataListProcessor);
    }

    public <T> long batchManagedProcess(String index, String[] fields, int scrollSize, Sort sort, QueryBuilder queryBuilder, Class<T> objectClass, ManagedDataListProcessor<T> managedDataListProcessor) {
        Scroll<T> scroll = findScroll(index, fields, scrollSize, sort, queryBuilder, objectClass);
        long itemIndex = 1;
        boolean isContinue = true;
        while (isContinue && !scroll.isEnd()) {
            long size = scroll.getContent().size();
            long from = itemIndex;
            long to = from + size - 1;
            isContinue = managedDataListProcessor.process(scroll.getContent(), from, to, scroll.getTotalElements());
            if (!isContinue) {
                return scroll.getTotalElements();
            }
            itemIndex = itemIndex + size;
            scroll = findScroll(scroll.getScrollId(), objectClass);
        }
        return scroll.getTotalElements();
    }

    public AggregationReport aggregation(String index, QueryBuilder queryBuilder, List<AggregationQuery> aggregationQueryList) {
        SearchRequest request = new SearchRequest(index);

        SearchSourceBuilder searchRequestBuilder = new SearchSourceBuilder();
        searchRequestBuilder.size(0);
        searchRequestBuilder.from(0);
        searchRequestBuilder.query(queryBuilder);

        request.source(searchRequestBuilder);

        List<AggregationBuilder> aggregationBuilders = ElasticSearchAggregationBuilder.build(aggregationQueryList);
        aggregationBuilders.forEach(searchRequestBuilder::aggregation);

        SearchResponse searchResponse;
        try {
            searchResponse = client.search(request, options);
        } catch (IOException e) {
            throw new RuntimeException("Bulk find document from elasticSearch error.", e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(searchResponse.toString());
        }
        Aggregations aggregations = searchResponse.getAggregations();

        AggregationReport aggregationReport = new AggregationReport();
        for (AggregationQuery aggregationQuery : aggregationQueryList) {
            AggregationTable aggregationTable = aggregationQuery.getAggregationQueryDate() != null ?
                    buildDateAggregationTable(aggregationQuery, aggregations) :
                    buildNormalAggregationTable(aggregationQuery, aggregations);
            aggregationReport.appendTable(aggregationTable);
        }
        logger.debug("执行结果：{}", JSON.toJsonString(aggregationReport));
        return aggregationReport;
    }

    private AggregationTable buildDateAggregationTable(AggregationQuery aggregationQuery, Aggregations aggregations) {
        AggregationTable table = new AggregationTable();
        String aggregationName = aggregationQuery.getAggregationQueryDate().getName();
        table.setName(aggregationName);
        ParsedDateHistogram internalDateHistogram = NestedPath.isNestedPath(aggregationQuery.getAggregationQueryDate().getField())
                ? ((ParsedNested) aggregations.get(aggregationName)).getAggregations().get(aggregationName)
                : aggregations.get(aggregationName);

        internalDateHistogram.getBuckets().forEach(bucket -> {
            String headerName = bucket.getKeyAsString();
            table.appendHeaded(headerName);
        });

        String aggQueryType = aggregationQuery.getType();
        if (Objects.equals(aggQueryType, AggregationQueryType.TERMS)) {
            Map<String, List<Double>> rowMap = Maps.newLinkedHashMap();

            int pos = 0;
            for (MultiBucketsAggregation.Bucket dateBucket : internalDateHistogram.getBuckets()) {
                ParsedStringTerms internalTerms = dateBucket.getAggregations().get(aggregationQuery.getName());
                for (Terms.Bucket termBucket : internalTerms.getBuckets()) {
                    setRowMap(rowMap, termBucket, pos);
                }
                pos++;
            }
            fillRowMap(rowMap, internalDateHistogram.getBuckets().size());
            appendRow(table, rowMap);
        } else {
            AggregationTable.Row row = new AggregationTable.Row();
            row.setName(aggregationQuery.getName());
            internalDateHistogram.getBuckets().forEach(dataBucket -> {
                Aggregation aggregation = dataBucket.getAggregations().get(aggregationQuery.getName());
                Double value = getAggregationValue(aggQueryType, aggregation);
                row.appendValue(value != null ? value : 0);
            });
            table.appendRow(row);
        }
        return table;
    }


    private AggregationTable buildNormalAggregationTable(AggregationQuery aggregationQuery, Aggregations aggregations) {
        String aggQueryType = aggregationQuery.getType();
        AggregationTable table = new AggregationTable();
        table.setName(aggregationQuery.getName());
        table.appendHeaded(aggQueryType);

        Aggregation aggregation = aggregations.get(aggregationQuery.getName());

        if (Objects.equals(aggQueryType, AggregationQueryType.TERMS)) {
            Map<String, List<Double>> rowMap = Maps.newLinkedHashMap();
            aggregation = getNestedAggregation(aggregation);
            if (aggregation instanceof ParsedTerms) {
                ParsedTerms internalTerms = (ParsedTerms) aggregation;
                for (Terms.Bucket termBucket : internalTerms.getBuckets()) {
                    setRowMap(rowMap, termBucket);
                }
                appendRow(table, rowMap);
            } else {
                logger.warn("数据结构错误 TERMS：{}", JSON.toJsonString(aggregation));
            }

        } else {
            AggregationTable.Row row = new AggregationTable.Row();
            row.setName(aggregationQuery.getName());

            Double value = getAggregationValue(aggQueryType, aggregation);
            row.appendValue(value != null ? value : 0);
            table.appendRow(row);
        }

        return table;
    }

    private Double getAggregationValue(String aggQueryType, Aggregation aggregation) {
        if (aggregation instanceof ParsedReverseNested) {
            ParsedReverseNested parsedReverseNested = (ParsedReverseNested) aggregation;
            aggregation = parsedReverseNested.getAggregations().get(aggregation.getName());
        }
        aggregation = getNestedAggregation(aggregation);
        switch (aggQueryType) {
            case AggregationQueryType.COUNT:
                return ((ParsedValueCount) aggregation).value();

            case AggregationQueryType.CARDINALITY:
                return ((ParsedCardinality) aggregation).value();

            case AggregationQueryType.AVG:
            case AggregationQueryType.SUM:
            case AggregationQueryType.MIN:
            case AggregationQueryType.MAX:
                return ((ParsedSingleValueNumericMetricsAggregation) aggregation).value();

        }
        return null;
    }

    private Aggregation getNestedAggregation(Aggregation aggregation) {
        if (aggregation instanceof ParsedNested) {
            ParsedNested nested = (ParsedNested) aggregation;
            aggregation = nested.getAggregations().get(aggregation.getName());
        }
        return aggregation;
    }

    private void setRowMap(Map<String, List<Double>> rowMap, Terms.Bucket bucket) {
        String key = bucket.getKeyAsString();
        double value = bucket.getDocCount();
        rowMap.computeIfAbsent(key, k -> Lists.newArrayList());
        rowMap.get(key).add(value);
    }


    private void setRowMap(Map<String, List<Double>> rowMap, Terms.Bucket bucket, int pos) {
        String key = bucket.getKeyAsString();
        double value = bucket.getDocCount();
        rowMap.computeIfAbsent(key, k -> Lists.newArrayList());
        List<Double> list = rowMap.get(key);
        while (list.size() < pos) {
            list.add(0d);
        }
        list.add(value);
    }

    private void fillRowMap(Map<String, List<Double>> rowMap, int len) {
        rowMap.forEach((name, list) -> {
            while (list.size() < len) {
                list.add(0d);
            }
        });
    }


    private void appendRow(AggregationTable table, Map<String, List<Double>> rowMap) {
        rowMap.forEach((name, list) -> {
            AggregationTable.Row row = new AggregationTable.Row();
            row.setName(name);
            row.setValues(list);
            table.appendRow(row);
        });
    }

    public void putTemplate(String name, List<String> patterns, String mapping) {
        putTemplate(name, patterns, mapping, buildSettings(1));
    }

    public void putTemplate(String name, List<String> patterns, String mapping, Settings settings) {
        PutIndexTemplateRequest request = new PutIndexTemplateRequest(name)
                .patterns(patterns)
                .settings(settings)
                .mapping(mapping, XContentType.JSON);

        try {
            client.indices().putTemplate(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Put index template error.", e);
        }
    }

    public boolean existTemplate(String name) {
        IndexTemplatesExistRequest request = new IndexTemplatesExistRequest(name);

        try {
            return client.indices().existsTemplate(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Check index template error.", e);
        }
    }

    public GetIndexTemplatesResponse getTemplate(String name) {
        GetIndexTemplatesRequest request = new GetIndexTemplatesRequest(name);

        try {
            return client.indices().getIndexTemplate(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Get index template error.", e);
        }
    }

    public void deleteTemplate(String name) {
        if (!existTemplate(name)) {
            return;
        }

        DeleteIndexTemplateRequest request = new DeleteIndexTemplateRequest()
                .name(name);
        try {
            client.indices().deleteTemplate(request, options);
        } catch (Exception e) {
            throw new RuntimeException("Delete index template error.", e);
        }
    }

    public interface DataProcessor<T> {
        void process(T t, long itemIndex, long itemCount);
    }

    public interface DataListProcessor<T> {
        void process(List<T> list, long from, long to, long itemCount);
    }

    public interface ManagedDataProcessor<T> {
        boolean process(T t, long itemIndex, long itemCount);
    }

    public interface ManagedDataListProcessor<T> {
        boolean process(List<T> list, long from, long to, long itemCount);
    }

}
