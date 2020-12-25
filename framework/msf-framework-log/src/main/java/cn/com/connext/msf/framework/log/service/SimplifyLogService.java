package cn.com.connext.msf.framework.log.service;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.log.entity.RequestLogInfo;
import cn.com.connext.msf.framework.log.entity.SystemLogInfo;
import cn.com.connext.msf.framework.query.QueryInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "es.enable", havingValue = "false", matchIfMissing = true)
public class SimplifyLogService implements BaseLogService {
    @Override
    public Page<ObjectNode> findPage(String index, QueryInfo queryInfo) {
        throw new BusinessException("未开启es配置，功能暂不可用。");
    }

    @Override
    public RequestLogInfo buildRequestLogInfo(QueryInfo queryInfo, String fromDate, String toDate, String field, String dateHistogramInterval) {
        throw new BusinessException("未开启es配置，功能暂不可用。");
    }

    @Override
    public SystemLogInfo buildSystemLogInfo(QueryInfo queryInfo, String fromDate, String toDate, String field, String dateHistogramInterval) {
        throw new BusinessException("未开启es配置，功能暂不可用。");
    }
}
