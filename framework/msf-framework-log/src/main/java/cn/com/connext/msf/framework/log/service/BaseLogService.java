package cn.com.connext.msf.framework.log.service;

import cn.com.connext.msf.framework.log.entity.RequestLogInfo;
import cn.com.connext.msf.framework.log.entity.SystemLogInfo;
import cn.com.connext.msf.framework.query.QueryInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;

public interface BaseLogService {
    Page<ObjectNode> findPage(String index, QueryInfo queryInfo);

    RequestLogInfo buildRequestLogInfo(QueryInfo queryInfo, String fromDate, String toDate, String field, String dateHistogramInterval);

    SystemLogInfo buildSystemLogInfo(QueryInfo queryInfo, String fromDate, String toDate, String field, String dateHistogramInterval);
}

