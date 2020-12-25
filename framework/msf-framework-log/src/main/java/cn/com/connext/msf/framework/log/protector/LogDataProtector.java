package cn.com.connext.msf.framework.log.protector;

import cn.com.connext.msf.framework.query.QueryInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class LogDataProtector {

    public QueryInfo resetQueryInfo(String level, QueryInfo queryInfo) {

        QueryInfo newQueryInfo = new QueryInfo();
        newQueryInfo.setPageable(queryInfo.getPageable());
        if (StringUtils.isBlank(queryInfo.getExpression())) {
            newQueryInfo.setExpression("level like " + level);
        } else {
            newQueryInfo.setExpression(queryInfo.getExpression() + " AND level like " + level);
        }
        return newQueryInfo;
    }
}
