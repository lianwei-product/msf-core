package cn.com.connext.msf.framework.log.service;

import cn.com.connext.msf.framework.agg.AggregationQuery;
import cn.com.connext.msf.framework.agg.AggregationQueryDate;
import cn.com.connext.msf.framework.agg.AggregationQueryType;
import cn.com.connext.msf.framework.agg.AggregationReport;
import cn.com.connext.msf.framework.data.elasticsearch.builder.ElasticSearchQueryBuilder;
import cn.com.connext.msf.framework.data.elasticsearch.repository.ConnextElasticSearchRepository;
import cn.com.connext.msf.framework.entity.ConnextEntityInfo;
import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.log.entity.RequestLogInfo;
import cn.com.connext.msf.framework.log.entity.SearchEntityInfo;
import cn.com.connext.msf.framework.log.entity.SystemLogInfo;
import cn.com.connext.msf.framework.log.protector.LogDataProtector;
import cn.com.connext.msf.framework.log.provider.UserProvider;
import cn.com.connext.msf.framework.query.QueryInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "es.enable", havingValue = "true", matchIfMissing = false)
public class EsLogService implements BaseLogService {

    private final ConnextElasticSearchRepository connextElasticSearchRepository;
    private final LogDataProtector logDataProtector;
    private final UserProvider userProvider;

    public EsLogService(ConnextElasticSearchRepository connextElasticSearchRepository,
                        LogDataProtector logDataProtector, UserProvider userProvider) {
        this.connextElasticSearchRepository = connextElasticSearchRepository;
        this.logDataProtector = logDataProtector;
        this.userProvider = userProvider;
    }


    public Page<ObjectNode> findPage(String index, QueryInfo queryInfo) {

        //ConnextEntityInfo connextEntityInfo = ConnextEntityManager.getConnextEntityInfo(SystemLogInfo.class);
        ConnextEntityInfo connextEntityInfo = SearchEntityInfo.from(index);
        QueryBuilder queryBuilder = ElasticSearchQueryBuilder.build(connextEntityInfo, queryInfo);

        String[] fieldArrays = SearchEntityInfo.toFileArray(connextEntityInfo);
        Page<ObjectNode> objectNodePage = connextElasticSearchRepository.findPage(index, fieldArrays, queryInfo.getPageable(), queryBuilder, ObjectNode.class);

        if (index.indexOf("request-trace") >= 0) {
            objectNodePage.getContent().forEach(objectNode -> {
                String userId = objectNode.findValue("user_id").asText(null);

                objectNode.put("user_name", userProvider.findName(userId));

            });
        }
        return objectNodePage;
    }

    public RequestLogInfo buildRequestLogInfo(QueryInfo queryInfo, String fromDate, String toDate, String field, String dateHistogramInterval) {

        String index = "request-trace-*";
        AggregationQueryDate date = AggregationQueryDate.from("countByDay", "@timestamp", dateHistogramInterval, 1, "yyyy-MM-dd HH:mm:ss", fromDate, toDate);
        AggregationQuery query01 = AggregationQuery.from("dayCount", field, AggregationQueryType.COUNT, date);
        //AggregationQuery query02 = AggregationQuery.from("termCount", field, AggregationQueryType.TERMS);

        ConnextEntityInfo connextEntityInfo = SearchEntityInfo.from(index);
        QueryBuilder queryBuilder = ElasticSearchQueryBuilder.build(connextEntityInfo, queryInfo);
        AggregationReport report = null;
        try {
            report = connextElasticSearchRepository.aggregation(index, queryBuilder, Lists.newArrayList(query01));
        } catch (NullPointerException e) {
            throw new BusinessException("请求日志索引缺失，请联系管理员。");
        }
        List<Integer> countList = buildCounts(0, report);
        List<String> headerList = buildHeaders(0, report);
        //Map<String, Integer> countMap = buildCountMap(1, report);

        RequestLogInfo requestLogInfo = RequestLogInfo.from(headerList, countList, null, null);
        return requestLogInfo;
    }

    public SystemLogInfo buildSystemLogInfo(QueryInfo queryInfo, String fromDate, String toDate, String field, String dateHistogramInterval) {

        SystemLogInfo systemLogInfo = new SystemLogInfo();
        String index = "logstash-*";
        setSystemLogCount(index, logDataProtector.resetQueryInfo("INFO", queryInfo), fromDate, toDate, field, systemLogInfo, "INFO", dateHistogramInterval);
        setSystemLogCount(index, logDataProtector.resetQueryInfo("ERROR", queryInfo), fromDate, toDate, field, systemLogInfo, "ERROR", dateHistogramInterval);
        setSystemLogCount(index, logDataProtector.resetQueryInfo("DEBUG", queryInfo), fromDate, toDate, field, systemLogInfo, "DEBUG", dateHistogramInterval);
        setSystemLogCount(index, logDataProtector.resetQueryInfo("WARN", queryInfo), fromDate, toDate, field, systemLogInfo, "WARN", dateHistogramInterval);

        return systemLogInfo;
    }

    private void setSystemLogCount(String index, QueryInfo queryInfo, String fromDate, String toDate, String field, SystemLogInfo systemLogInfo, String type, String dateHistogramInterval) {

        AggregationQueryDate date = AggregationQueryDate.from("countByDay", "@timestamp", dateHistogramInterval, 1, "yyyy-MM-dd HH:mm:ss", fromDate, toDate);
        AggregationQuery query01 = AggregationQuery.from("dayCount", field, AggregationQueryType.COUNT, date);

        ConnextEntityInfo connextEntityInfo = SearchEntityInfo.from(index);
        QueryBuilder queryBuilder = ElasticSearchQueryBuilder.build(connextEntityInfo, queryInfo);
        AggregationReport report = null;
        try {
            report = connextElasticSearchRepository.aggregation(index, queryBuilder, Lists.newArrayList(query01));
        } catch (NullPointerException e) {
            throw new BusinessException("系统日志索引缺失，请联系管理员。");
        }
        List<Integer> countList = Lists.newArrayList();
        report.getTables().get(0).getRows().get(0).getValues().forEach(n -> {
            countList.add(n.intValue());
        });
        List<String> headers = buildHeaders(0, report);
        systemLogInfo.setAxisNameList(headers);
        switch (type) {
            case "INFO":
                systemLogInfo.setInfoCountList(countList);
                break;
            case "ERROR":
                systemLogInfo.setErrorCountList(countList);
                break;
            case "DEBUG":
                systemLogInfo.setDebugCountList(countList);
                break;
            case "WARN":
                systemLogInfo.setWarnCountList(countList);
                break;
        }
    }

    private List<String> buildHeaders(int reportIndex, AggregationReport report) {

        List<String> headers = Lists.newArrayList();
        report.getTables().get(reportIndex).getHeader().forEach(header -> {
            headers.add(header);
        });
        return headers;
    }

    private List<Integer> buildCounts(int reportIndex, AggregationReport report) {

        List<Integer> countList = Lists.newArrayList();
        report.getTables().get(reportIndex).getRows().get(0).getValues().forEach(n -> {
            countList.add(n.intValue());
        });
        return countList;
    }

    private Map<String, Integer> buildCountMap(int reportIndex, AggregationReport report) {

        Map<String, Integer> countMap = new HashMap<>();

        report.getTables().get(reportIndex).getRows().forEach(row -> {
            if (StringUtils.isNotEmpty(row.getName())) {
                countMap.put(row.getName(), row.getValues().get(0).intValue());
            }
        });

        //排序
        Map<String, Integer> newCountMap = countMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return newCountMap;
    }

}
