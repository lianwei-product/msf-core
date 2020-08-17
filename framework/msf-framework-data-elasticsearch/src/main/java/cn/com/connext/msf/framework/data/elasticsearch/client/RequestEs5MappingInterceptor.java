package cn.com.connext.msf.framework.data.elasticsearch.client;

import cn.com.connext.msf.framework.utils.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Apache HttpClient 请求拦截器
 * 用于兼容ES5.X中index的type类型
 */
public class RequestEs5MappingInterceptor implements ElasticSearchRequestInterceptor {
    private final Logger logger = LoggerFactory.getLogger(RequestEs5MappingInterceptor.class);

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        HttpRequestWrapper requestWrapper = (HttpRequestWrapper) request;
        URIBuilder uriBuilder = new URIBuilder(requestWrapper.getURI());

        String requestMethod = requestWrapper.getMethod();
        switch (requestMethod) {
            case "PUT":
                if (uriBuilder.getPath().endsWith("_mapping")) {
                    rebuildMappingUri(requestWrapper, uriBuilder);
                }
                break;

            case "POST":
//                if (uriBuilder.getPath().endsWith("_search")) {
//                    rebuildTermsContent(requestWrapper, uriBuilder);
//                }
                if (uriBuilder.getPath().endsWith("_bulk")) {
                    rebuildBulkUri(requestWrapper, uriBuilder);
                }

                break;
        }
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean isMatch(int primaryVersion) {
        return primaryVersion == 5;
    }

    /**
     * 重新构建修改ES mapping请求Uri
     *
     * @param requestWrapper
     * @param uriBuilder
     */
    private void rebuildMappingUri(HttpRequestWrapper requestWrapper, URIBuilder uriBuilder) {
        uriBuilder.setPath(uriBuilder.getPath() + "/doc");
        RequestParamsInterceptor.rebuildRequestUri(requestWrapper, uriBuilder, logger);
    }

    /**
     * 重新构聚合terms内容
     *
     * @param requestWrapper
     * @param uriBuilder
     */
    private void rebuildTermsContent(HttpRequestWrapper requestWrapper, URIBuilder uriBuilder) throws IOException {
        HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) requestWrapper;
        HttpEntity entity = entityEnclosingRequest.getEntity();
        String httpContent = EntityUtils.toString(entity);
        if (StringUtils.isNotEmpty(httpContent) && httpContent.contains("_key")) {
            ObjectNode node = JSON.parseObject(httpContent, ObjectNode.class);
            JsonNode orderNode = node.findValue("order");
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            if (orderNode != null) {
                orderNode.forEach(childNode -> {
                    JsonNode keyNode = childNode.findValue("_key");
                    if (keyNode == null) {
                        arrayNode.add(childNode);
                    }
                });

                ObjectNode parentNode = node.findParent("order");
                parentNode.set("order", arrayNode);
            }

            entity = new StringEntity(JSON.toJsonString(node), ContentType.create("application/json"));
            entityEnclosingRequest.setEntity(entity);
        }
    }

    /**
     * 重新构建批量请求Uri
     *
     * @param requestWrapper
     * @param uriBuilder
     * @throws IOException
     */
    private void rebuildBulkUri(HttpRequestWrapper requestWrapper, URIBuilder uriBuilder) throws IOException {
        HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) requestWrapper;
        HttpEntity entity = entityEnclosingRequest.getEntity();
        String httpContent = EntityUtils.toString(entity);
        String[] contentList = StringUtils.split(httpContent, "\n");
        if (contentList.length > 0) {
            String content = contentList[0];
            ObjectNode objectNode = JSON.parseObject(content, ObjectNode.class);
            String indexName = objectNode.findValue("_index").asText(null);
            if (StringUtils.isNotEmpty(indexName)) {
                uriBuilder.setPath("/" + indexName + "/doc" + uriBuilder.getPath());
                RequestParamsInterceptor.rebuildRequestUri(requestWrapper, uriBuilder, logger);
            }
        }
    }
}
