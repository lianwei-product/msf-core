package cn.com.connext.msf.framework.data.elasticsearch.client;

import com.google.common.collect.Sets;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

/**
 * Apache HttpClient 请求拦截器
 * ElasticSearch官方所提供的RestHighLevelClient（7.x版本），在构建Rest的请求时，会包含5.X系统ES所不支持的参数。
 * 本拦截器会在RestHighLevelClient 向ES服务器发起http请求时，删除掉相关参数。
 */
public class RequestParamsInterceptor implements ElasticSearchRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(RequestParamsInterceptor.class);
    private final Set<String> notSupportParams = Sets.newHashSet("ccs_minimize_roundtrips", "ignore_throttled");

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        HttpRequestWrapper requestWrapper = (HttpRequestWrapper) request;

        if (logger.isDebugEnabled()) {
            logger.debug("Source uri: {}", requestWrapper.getRequestLine().toString());
        }

        URIBuilder uriBuilder = new URIBuilder(requestWrapper.getURI());
        List<NameValuePair> params = uriBuilder.getQueryParams();
        params.removeIf(param -> notSupportParams.contains(param.getName()));
        uriBuilder.clearParameters().addParameters(params);

        rebuildRequestUri(requestWrapper, uriBuilder, logger);
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean isMatch(int primaryVersion) {
        return primaryVersion == 5 || primaryVersion == 6;
    }

    /**
     * 重新构建请求Uri
     *
     * @param requestWrapper
     * @param uriBuilder
     */
    public static void rebuildRequestUri(HttpRequestWrapper requestWrapper, URIBuilder uriBuilder, Logger logger) {
        try {
            requestWrapper.setURI(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Target uri: {}", requestWrapper.getRequestLine().toString());
        }
    }
}
