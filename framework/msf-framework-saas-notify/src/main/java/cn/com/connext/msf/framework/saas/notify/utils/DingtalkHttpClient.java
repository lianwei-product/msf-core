package cn.com.connext.msf.framework.saas.notify.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DingtalkHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(DingtalkHttpClient.class);

    private final static int MAX_CONNECT_TIMEOUT = 2000;
    private final static int MAX_REQUEST_TIMEOUT = 10000;
    private final static int MAX_SOCKET_TIMEOUT = 30000;
    private final static int MAX_REQUEST_RETRY_TIMES = 3;
    private final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static RestTemplate restTemplate;

    static {
        //生成一个设置了连接超时时间、请求超时时间、异常重试次数3次
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(MAX_REQUEST_TIMEOUT).setConnectTimeout(MAX_CONNECT_TIMEOUT).setSocketTimeout(MAX_SOCKET_TIMEOUT).build();
        //重试。
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(config).setRetryHandler(new DefaultHttpRequestRetryHandler(MAX_REQUEST_RETRY_TIMES, true));
        HttpClient httpClient = builder.build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);
        List<HttpMessageConverter<?>> converterList = new ArrayList<>();
        converterList.add(new FormHttpMessageConverter());
        converterList.add(new ByteArrayHttpMessageConverter());
        converterList.add(new StringHttpMessageConverter(DEFAULT_CHARSET));
        restTemplate.setMessageConverters(converterList);
    }

    public static String post(String url, String content) {
        return post(url, content, MAX_REQUEST_RETRY_TIMES);
    }

    private static String post(String url, String content, int retryTimes) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(MAX_CONNECT_TIMEOUT).setSocketTimeout(MAX_SOCKET_TIMEOUT).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        if (content != null) {
            StringEntity stringEntity = new StringEntity(content, DEFAULT_CHARSET);
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("status code is not 200.");
            }
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            if (retryTimes > 0) {
                int nextRetryTimes = retryTimes - 1;
                return post(url, content, nextRetryTimes);
            }
            throw new RuntimeException("Http post request error, message: " + e.getMessage());
        } finally {
            try {
                ((CloseableHttpClient) httpClient).close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

}
