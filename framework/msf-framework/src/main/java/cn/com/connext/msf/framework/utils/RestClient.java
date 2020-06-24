package cn.com.connext.msf.framework.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestClient {

    private final static int MAX_CONNECT_TIMEOUT = 10000;
    private final static int MAX_REQUEST_TIMEOUT = 60000;
    private final static int MAX_SOCKET_TIMEOUT = 120000;

    private final RestTemplate restTemplate;

    public RestClient() {
        this(MAX_CONNECT_TIMEOUT, MAX_REQUEST_TIMEOUT, MAX_SOCKET_TIMEOUT);
    }

    public RestClient(int maxConnectTimeout, int maxRequestTimeout, int maxSocketTimeout) {
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(maxRequestTimeout)
                .setConnectTimeout(maxConnectTimeout)
                .setSocketTimeout(maxSocketTimeout)
                .build();

        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(config);
        HttpClient httpClient = builder.build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        restTemplate = new RestTemplate(requestFactory);
    }

    public <ResponseBody> ResponseBody get(String url, Class<ResponseBody> responseBodyClass) {
        return exchange(HttpMethod.GET, url, null, null, responseBodyClass);
    }

    public <ResponseBody> ResponseBody get(String url, ParameterizedTypeReference<ResponseBody> typeReference) {
        return exchange(HttpMethod.GET, url, null, null, typeReference);
    }

    public <RequestBody> void post(String url, MultiValueMap<String, String> headers, RequestBody requestBody) {
        exchange(HttpMethod.POST, url, headers, requestBody, Void.class);
    }


    private <RequestBody, ResponseBody> ResponseBody exchange(HttpMethod httpMethod,
                                                              String url,
                                                              MultiValueMap<String, String> headers,
                                                              RequestBody requestBody,
                                                              Class<ResponseBody> responseClass) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(requestBody, headers), responseClass).getBody();
    }

    private <RequestBody, ResponseBody> ResponseBody exchange(HttpMethod httpMethod,
                                                              String url,
                                                              MultiValueMap<String, String> headers,
                                                              RequestBody requestBody,
                                                              ParameterizedTypeReference<ResponseBody> typeReference) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(requestBody, headers), typeReference).getBody();
    }

}
