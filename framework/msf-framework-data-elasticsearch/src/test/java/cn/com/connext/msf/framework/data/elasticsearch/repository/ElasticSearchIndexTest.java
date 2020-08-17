package cn.com.connext.msf.framework.data.elasticsearch.repository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.framework.data.elasticsearch.client.RestHighLevelClientBuilder;
import cn.com.connext.msf.framework.data.elasticsearch.utils.DemoMappingBuilder;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchIndexTest {

    private final String indexName = "demo";
    private final ConnextElasticSearchRepository repository;

    public ElasticSearchIndexTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.INFO);
        loggerContext.getLogger("cn.com.connext").setLevel(Level.DEBUG);

        RestHighLevelClient restHighLevelClient = RestHighLevelClientBuilder.build("192.168.0.10", 9200);
        repository = new ConnextElasticSearchRepository(restHighLevelClient, RequestOptions.DEFAULT);
    }

    @Test
    public void ensureIndex() {
        repository.ensureIndex(indexName, DemoMappingBuilder.build());
        boolean exist = repository.existIndex(indexName);
        Assert.assertEquals(true, exist);
    }

    @Test
    public void createIndex() {
        repository.deleteIndex(indexName);
        repository.createIndex(indexName, DemoMappingBuilder.build());
        boolean exist = repository.existIndex(indexName);
        Assert.assertEquals(true, exist);
    }

    @Test
    public void modifyIndex() {
        repository.ensureIndex(indexName, DemoMappingBuilder.build());
        repository.modifyIndex(indexName, DemoMappingBuilder.build());
    }

    @Test
    public void deleteIndex() {
        repository.ensureIndex(indexName, DemoMappingBuilder.build());
        repository.deleteIndex(indexName);
        boolean exist = repository.existIndex(indexName);
        Assert.assertEquals(false, exist);
    }

    @Test
    public void existIndex() {
        repository.ensureIndex(indexName, DemoMappingBuilder.build());
        boolean exist = repository.existIndex(indexName);
        Assert.assertEquals(true, exist);
    }

}