package cn.com.connext.msf.framework.data.elasticsearch.config;

import cn.com.connext.msf.framework.data.elasticsearch.client.RestHighLevelClientBuilder;
import cn.com.connext.msf.framework.data.elasticsearch.repository.ConnextElasticSearchRepository;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "es.enable", havingValue = "true", matchIfMissing = true)
@SuppressWarnings("SpringFacetCodeInspection")
public class ConnextElasticSearchRepositoryConfig {

    private final Logger logger = LoggerFactory.getLogger(ConnextElasticSearchRepositoryConfig.class);
    private final List<HttpHost> httpHostList;
    private final String credentials;
    private final RequestOptions requestOptions;


    public ConnextElasticSearchRepositoryConfig(@Value("${es.host}") String host,
                                                @Value("${es.username}") String username,
                                                @Value("${es.password}") String password) {
        this.httpHostList = getHttpHostList(host);
        this.credentials = getCredentials(username, password);
        this.requestOptions = getRequestOptions(credentials);
    }

    @Bean
    public ConnextElasticSearchRepository connextElasticSearchRepository(RestHighLevelClient restHighLevelClient) {
        return new ConnextElasticSearchRepository(restHighLevelClient, requestOptions);
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        logger.info("Init RestHighLevelClient");
        try {
            HttpHost[] httpHostArray = httpHostList.toArray(new HttpHost[httpHostList.size()]);
            return RestHighLevelClientBuilder.build(httpHostArray, credentials);
        } catch (Exception e) {
            logger.error("Init RestHighLevelClient error.", e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RequestOptions requestOptions() {
        return requestOptions;
    }

    private List<HttpHost> getHttpHostList(String host) {
        List<HttpHost> httpHostList = Lists.newArrayList();
        String[] hostArray = StringUtils.split(host, ",");

        for (String hostInfo : hostArray) {
            String[] infoArray = StringUtils.split(hostInfo, ":");
            httpHostList.add(new HttpHost(infoArray[0], Integer.parseInt(infoArray[1])));
        }

        return httpHostList;
    }

    private String getCredentials(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }

        return "Basic ".concat(Base64Utils.encodeToString(username.concat(":").concat(password).getBytes(Charset.forName("UTF-8"))));
    }

    private RequestOptions getRequestOptions(String credentials) {
        if (StringUtils.isBlank(credentials)) {
            return RequestOptions.DEFAULT;
        }

        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("Authorization", credentials);
        return builder.build();
    }

    List<HttpHost> getHttpHostList() {
        return httpHostList;
    }

    RequestOptions getRequestOptions() {
        return requestOptions;
    }
}
