package cn.com.connext.msf.framework.metrics;

public final class RequestTraceTypes {

    /**
     * 无
     */
    public static final String NONE = "none";

    /**
     * 信息 - 包含请求接口
     */
    public static final String INFO = "info";

    /**
     * 调试 - 包含入参、出参信息
     */
    public static final String DEBUG = "debug";


    /**
     * 追踪 - 包含完整调用链信息
     */
    public static final String TRACE = "trace";

}
