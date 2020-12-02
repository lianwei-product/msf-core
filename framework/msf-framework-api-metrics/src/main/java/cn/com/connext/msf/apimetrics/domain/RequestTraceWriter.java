package cn.com.connext.msf.apimetrics.domain;

import cn.com.connext.msf.apimetrics.entity.RequestTrace;

public interface RequestTraceWriter {

    void write(RequestTrace requestTrace);

}
