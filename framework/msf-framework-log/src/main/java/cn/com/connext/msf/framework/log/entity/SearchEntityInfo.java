package cn.com.connext.msf.framework.log.entity;

import cn.com.connext.msf.framework.entity.ConnextEntityInfo;
import cn.com.connext.msf.framework.query.QueryOperators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchEntityInfo {

    public static ConnextEntityInfo buildSystemLogInfo() {
        ConnextEntityInfo connextEntityInfo = new ConnextEntityInfo();
        connextEntityInfo.addField("app_name", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("@timestamp", "date", true, QueryOperators.DATE_OPERATORS);
        connextEntityInfo.addField("level", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("message", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("stack_trace", "text", false, QueryOperators.TEXT_OPERATORS);

        return connextEntityInfo;
    }

    public static ConnextEntityInfo buildRequestLogInfo() {
        ConnextEntityInfo connextEntityInfo = new ConnextEntityInfo();
        connextEntityInfo.addField("app_name", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("@timestamp", "date", true, QueryOperators.DATE_OPERATORS);
        connextEntityInfo.addField("current_route_id", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("request_begin_time", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("uri_method_map", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("trace_id", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("span_id", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("parent_span_id", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("tenant_id", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("bu_code", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("user_id", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("request_uri", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("real_ip", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("remote_addr", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("path", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("method", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("trace_type", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("time_took", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("request_parameters", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("request_body", "text", false, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("response_body", "text", false, QueryOperators.TEXT_OPERATORS);
        return connextEntityInfo;
    }

    public static String[] toFileArray(ConnextEntityInfo connextEntityInfo) {
        List<String> fieldLists = new ArrayList<>();

        connextEntityInfo.getFields().forEach((key, item) -> {
            fieldLists.add(item.getName());
        });
        String[] fieldArrays = fieldLists.toArray(new String[fieldLists.size()]);
        return fieldArrays;
    }

    public static ConnextEntityInfo from(String index) {
        ConnextEntityInfo connextEntityInfo = new ConnextEntityInfo();
        if (Objects.equals("logstash-*", index)) {
            connextEntityInfo = buildSystemLogInfo();
        } else if (Objects.equals("request-trace-*", index)) {
            connextEntityInfo = buildRequestLogInfo();
        }
        return connextEntityInfo;
    }
}
