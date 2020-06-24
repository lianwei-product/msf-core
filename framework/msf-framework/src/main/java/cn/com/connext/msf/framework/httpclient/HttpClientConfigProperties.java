package cn.com.connext.msf.framework.httpclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "httpclinet.config")
public class HttpClientConfigProperties {
    /**
     * 连接池大小
     **/
    private int connMaxPoolTotal = 200;
    /**
     * 链接生存时间（s）
     **/
    private int connLiveTime = 60;
    /**
     * 路由基数
     **/
    private int defaultMapPerRoute = 20;

    /**
     * 是否支持https
     **/
    private boolean htts;

    private int socketTimeout = 2000;
    private int connectTimeout = 2000;
    private int connectionRequestTimeout = 2000;

    private int keepAliveTime = 30;

    /**
     * 失败重试次数
     **/
    private int retryTime;

    /**
     * 连接代理
     **/
    private String proxyHost;
    private int porxyPort;


    public int getConnMaxPoolTotal() {
        return connMaxPoolTotal;
    }

    public void setConnMaxPoolTotal(int connMaxPoolTotal) {
        this.connMaxPoolTotal = connMaxPoolTotal;
    }

    public int getConnLiveTime() {
        return connLiveTime;
    }

    public void setConnLiveTime(int connLiveTime) {
        this.connLiveTime = connLiveTime;
    }

    public boolean isHtts() {
        return htts;
    }

    public void setHtts(boolean htts) {
        this.htts = htts;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getPorxyPort() {
        return porxyPort;
    }

    public void setPorxyPort(int porxyPort) {
        this.porxyPort = porxyPort;
    }

    public int getDefaultMapPerRoute() {
        return defaultMapPerRoute;
    }

    public void setDefaultMapPerRoute(int defaultMapPerRoute) {
        this.defaultMapPerRoute = defaultMapPerRoute;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
