package cn.com.connext.msf.framework.metrics;

public class RequestTraceRule {

    private String path;
    private String type;

    public static RequestTraceRule from(String path, String type) {
        RequestTraceRule requestTraceRule = new RequestTraceRule();
        requestTraceRule.path = path;
        requestTraceRule.type = type;
        return requestTraceRule;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }
}
