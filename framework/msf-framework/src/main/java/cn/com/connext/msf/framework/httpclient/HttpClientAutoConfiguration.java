package cn.com.connext.msf.framework.httpclient;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(HttpClientConfigProperties.class)
@ConditionalOnProperty(prefix = "httpclient", value = "enabled", matchIfMissing = true)
public class HttpClientAutoConfiguration {

    @Autowired
    HttpClientConfigProperties properties;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {

        PoolingHttpClientConnectionManager poolHttpConnManger = new PoolingHttpClientConnectionManager(
                properties.getConnLiveTime(),
                TimeUnit.SECONDS
        );

        // 最大连接数
        poolHttpConnManger.setMaxTotal(properties.getConnMaxPoolTotal());

        // 路由基数
        poolHttpConnManger.setDefaultMaxPerRoute(properties.getDefaultMapPerRoute());

        return poolHttpConnManger;

    }

    @Bean
    public RequestConfig config() {

        return RequestConfig.custom()
                .setConnectionRequestTimeout(properties.getConnectionRequestTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setSocketTimeout(properties.getSocketTimeout())
                .build();
    }

/*	@ConditionalOnProperty(prefix = "httpclient.config.proxyHost")
    @Bean
	public DefaultProxyRoutePlanner defaultProxyRoutePlanner(){
		HttpHost proxy = new HttpHost(properties.getProxyHost(), properties.getPorxyPort());
		return new DefaultProxyRoutePlanner(proxy);
	}*/


    @Bean
    public ConnectionKeepAliveStrategy myconnectionKeepAliveStrategy() {
        return new MyconnectionKeepAliveStrategy(properties.getKeepAliveTime());
    }


    @Bean
    public HttpRequestRetryHandler httpRequestRetryHandler() {

        return new MyHttpRequestRetryHandler(properties.getRetryTime());
    }

    @Bean
    public HttpClientManagerFactoryBen httpClientManagerFactoryBen() {
        return new HttpClientManagerFactoryBen();
    }


}

