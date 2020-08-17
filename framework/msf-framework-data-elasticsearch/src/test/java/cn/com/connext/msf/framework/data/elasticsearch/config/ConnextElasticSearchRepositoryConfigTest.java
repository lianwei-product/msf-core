package cn.com.connext.msf.framework.data.elasticsearch.config;

import cn.com.connext.msf.framework.utils.JSON;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConnextElasticSearchRepositoryConfigTest {

    private final Logger logger = LoggerFactory.getLogger(ConnextElasticSearchRepositoryConfigTest.class);

    @Test
    public void getHttpHostList() {
        ConnextElasticSearchRepositoryConfig config = new ConnextElasticSearchRepositoryConfig("192.168.0.10:9200,192.168.0.10:9200", null, null);
        List<HttpHost> httpHostList = config.getHttpHostList();
        logger.info(JSON.toIndentJsonString(httpHostList));
        Assert.assertEquals(2, httpHostList.size());

        config = new ConnextElasticSearchRepositoryConfig("192.168.0.10:9200", null, null);
        httpHostList = config.getHttpHostList();
        logger.info(JSON.toIndentJsonString(httpHostList));
        Assert.assertEquals(1, httpHostList.size());
    }

    @Test
    public void getRequestOptions() {
        ConnextElasticSearchRepositoryConfig config = new ConnextElasticSearchRepositoryConfig("192.168.0.10:9200", "abc", "123");
        RequestOptions requestOptions = config.getRequestOptions();
        List<Header> headers = requestOptions.getHeaders();
        Header header = headers.get(0);
        Assert.assertEquals("Authorization", header.getName());
        Assert.assertEquals("Basic YWJjOjEyMw==", header.getValue());
    }

}