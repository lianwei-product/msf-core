package cn.com.connext.msf.apimetrics.entity;

import cn.com.connext.msf.framework.metrics.RequestTraceTypes;

public class RequestTrace {


    private String traceId;

    private String spanId;

    private String parentSpanId;

    private String tenantId;

    private String buCode;

    private String userId;

    private String requestUri;

    private String remoteAddr;

    private String realIp;

    private String path;

    private String method;

    /**
     * {@link RequestTraceTypes}
     */
    private String traceType;

    private long timeTook;

    private String requestParameters;

    private String requestBody;

    private String responseBody;

    private static RequestTrace from(String traceId, String spanId, String parentSpanId,
                                    String tenantId, String buCode, String userId,
                                    String requestUri, String remoteAddr, String realIp, String path, String method,
                                    String logLevel, long millionSeconds) {
        RequestTrace requestTrace = new RequestTrace();
        requestTrace.traceId = traceId;
        requestTrace.spanId = spanId;
        requestTrace.parentSpanId = parentSpanId;
        requestTrace.tenantId = tenantId != null ? tenantId : "sys";
        requestTrace.buCode = buCode != null ? buCode : "sys";
        requestTrace.userId = userId != null ? userId : "anonymous";
        requestTrace.requestUri = requestUri;
        requestTrace.remoteAddr = remoteAddr;
        requestTrace.realIp = realIp;
        requestTrace.path = path;
        requestTrace.method = method;
        requestTrace.traceType = logLevel;
        requestTrace.timeTook = millionSeconds;
        return requestTrace;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    public void setParentSpanId(String parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId != null ? tenantId : "sys";
    }

    public String getBuCode() {
        return buCode;
    }

    public void setBuCode(String buCode) {
        this.buCode = buCode != null ? buCode : "sys";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId != null ? userId : "anonymous";
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRealIp() {
        return realIp;
    }

    public void setRealIp(String realIp) {
        this.realIp = realIp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTraceType() {
        return traceType;
    }

    public void setTraceType(String traceType) {
        this.traceType = traceType;
    }

    public long getTimeTook() {
        return timeTook;
    }

    public void setTimeTook(long timeTook) {
        this.timeTook = timeTook;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
