package cn.com.connext.msf.apimetrics.domain;

import cn.com.connext.msf.apimetrics.entity.RequestTrace;
import cn.com.connext.msf.framework.metrics.RequestTraceVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class LogstashWriter implements RequestTraceWriter {

    private final Logger logger = LoggerFactory.getLogger("RequestTrace");

    @Override
    public void write(RequestTrace requestTrace) {
        MDC.clear();
        MDC.put(RequestTraceVariables.TRACE_ID, requestTrace.getTraceId());
        MDC.put(RequestTraceVariables.SPAN_ID, requestTrace.getSpanId());
        MDC.put(RequestTraceVariables.PARENT_SPAN_ID, requestTrace.getParentSpanId());
        MDC.put(RequestTraceVariables.TENANT_ID, requestTrace.getTenantId());
        MDC.put(RequestTraceVariables.BU_CODE, requestTrace.getBuCode());
        MDC.put(RequestTraceVariables.USER_ID, requestTrace.getUserId());
        MDC.put(RequestTraceVariables.REQUEST_URI, requestTrace.getRequestUri());
        MDC.put(RequestTraceVariables.REAL_IP, requestTrace.getRealIp());
        MDC.put(RequestTraceVariables.REMOTE_ADDR, requestTrace.getRemoteAddr());
        MDC.put(RequestTraceVariables.PATH, requestTrace.getPath());
        MDC.put(RequestTraceVariables.METHOD, requestTrace.getMethod());
        MDC.put(RequestTraceVariables.TRACE_TYPE, requestTrace.getTraceType());
        MDC.put(RequestTraceVariables.TIME_TOOK, String.valueOf(requestTrace.getTimeTook()));
        MDC.put(RequestTraceVariables.REQUEST_PARAMETERS, requestTrace.getRequestParameters());
        MDC.put(RequestTraceVariables.REQUEST_BODY, requestTrace.getRequestBody());
        MDC.put(RequestTraceVariables.RESPONSE_BODY, requestTrace.getResponseBody());
        logger.info("");
    }

}
