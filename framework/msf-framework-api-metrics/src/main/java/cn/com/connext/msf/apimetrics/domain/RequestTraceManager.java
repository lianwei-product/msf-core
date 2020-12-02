package cn.com.connext.msf.apimetrics.domain;

import cn.com.connext.msf.apimetrics.entity.RequestTrace;
import org.springframework.stereotype.Component;

@Component
public class RequestTraceManager {

    private final RequestTraceWriter requestTraceWriter;

    public RequestTraceManager(RequestTraceWriter requestTraceWriter) {
        this.requestTraceWriter = requestTraceWriter;
    }

    public void save(RequestTrace requestTrace) {
        requestTraceWriter.write(requestTrace);
    }

}
