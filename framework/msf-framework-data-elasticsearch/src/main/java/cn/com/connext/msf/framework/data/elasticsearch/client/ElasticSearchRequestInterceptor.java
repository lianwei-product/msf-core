package cn.com.connext.msf.framework.data.elasticsearch.client;

import org.apache.http.HttpRequestInterceptor;

/**
 * Apache HttpClient 请求拦截器
 * 用于兼容ES5.X, ES6.X中index的type类型
 */
public interface ElasticSearchRequestInterceptor extends HttpRequestInterceptor {

    /**
     * @return 返回处理器执行次序，数值越小，排序越前。
     */
    default int priority() {
        return 0;
    }

    /**
     * @return 是否可以处理指定事件。
     */
    boolean isMatch(int primaryVersion);
}
