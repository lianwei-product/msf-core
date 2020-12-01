package cn.com.connext.msf.framework.data.elasticsearch.client;

import cn.com.connext.msf.framework.data.elasticsearch.config.ConnextElasticSearchFeature;
import com.google.common.collect.Lists;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Comparator;
import java.util.List;


public class RestHighLevelClientBuilder {

    private static List<ElasticSearchRequestInterceptor> interceptors;

    static {
        interceptors = Lists.newArrayList(
                new RequestParamsInterceptor(),
                new RequestEs5MappingInterceptor(),
                new RequestEs6MappingInterceptor()
        );
        interceptors.sort(Comparator.comparingInt(ElasticSearchRequestInterceptor::priority));
    }

    public static RestHighLevelClient build(String host, int port) {
        return build(host, port, null);
    }

    public static RestHighLevelClient build(String host, int port, String credentials) {
        return build(new HttpHost[]{new HttpHost(host, port)}, credentials);
    }

    public static RestHighLevelClient build(HttpHost[] httpHostArray, String credentials) {
        ConnextElasticSearchFeature.init(httpHostArray[0], credentials);

        RestClientBuilder restClientBuilder = RestClient.builder(httpHostArray);

        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(15000);
            requestConfigBuilder.setSocketTimeout(5 * 60000);
            requestConfigBuilder.setConnectionRequestTimeout(30000);
            return requestConfigBuilder;
        });

        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            for (ElasticSearchRequestInterceptor interceptor : interceptors) {
                if (interceptor.isMatch(ConnextElasticSearchFeature.getPrimaryVersion())) {
                    httpClientBuilder.addInterceptorFirst(interceptor);
                }
            }
            return httpClientBuilder;
        });

        return new RestHighLevelClient(restClientBuilder);
    }

}
