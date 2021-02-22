package cn.com.connext.msf.framework.metrics;

import java.util.Map;

public interface RequestTraceRuleProvider {

    /**
     *  {{"GET": {"path": "/api/xx/{id}", "type": "none"}, "DELETE": {"path": "/api/xx/{id}", "type": "trace"}}}
     */
    Map<String, RequestTraceRule> getRequestTraceRule(String uri, String method);
}
