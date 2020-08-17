package cn.com.connext.msf.framework.data.elasticsearch.config;

import cn.com.connext.msf.framework.utils.JSON;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ConnextElasticSearchFeature {

    private static Logger logger = LoggerFactory.getLogger(ConnextElasticSearchFeature.class);

    private static String version;

    /**
     * es主版本号
     */
    private static int primaryVersion;

    /**
     * 是否需要支持ES6.X之前版本中的type特性。
     * ES5.X中一个index下可以有多个type，该特性在ES6.X之后被移除
     */
    private static boolean needSupportTypeFeature;


    public static void init(HttpHost httpHost, String credentials) {
        version = getElasticSearchVersion(httpHost, credentials);
        primaryVersion = Integer.parseInt(StringUtils.split(version, ".")[0]);
        needSupportTypeFeature = version.startsWith("5") || version.startsWith("6");
    }

    public static String getVersion() {
        return version;
    }

    public static int getPrimaryVersion() {
        return primaryVersion;
    }


    /**
     * 是否需要支持ES6.X之前版本中的type特性
     */
    public static boolean needSupportTypeFeature() {
        return needSupportTypeFeature;
    }

    @SuppressWarnings("deprecation")
    public static void prepareRequestType(DocWriteRequest request) {
        if (ConnextElasticSearchFeature.getPrimaryVersion() <= 5) {
            request.type("doc");
        }
    }

    @SuppressWarnings("deprecation")
    public static void prepareRequestType(GetRequest request) {
        if (ConnextElasticSearchFeature.getPrimaryVersion() <= 5) {
            request.type("doc");
        }
    }

    private static String getElasticSearchVersion(HttpHost httpHost, String credentials) {
        HttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setSocketTimeout(3000).build();
        String uri = httpHost.toURI();
        HttpGet httpGet = new HttpGet(uri);

        if (!StringUtils.isBlank(credentials)) {
            httpGet.addHeader("Authorization", credentials);
        }

        httpGet.setConfig(requestConfig);

        String content = "";
        try {
            HttpResponse response = httpClient.execute(httpGet);
            content = EntityUtils.toString(response.getEntity(), "utf-8");
            ObjectNode objectNode = JSON.parseObject(content, ObjectNode.class);
            return objectNode.get("version").get("number").asText("");
        } catch (Exception e) {
            throw new RuntimeException("Can not get elasticSearch version, host: " + uri + ", content:" + content, e);
        } finally {
            try {
                ((CloseableHttpClient) httpClient).close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
