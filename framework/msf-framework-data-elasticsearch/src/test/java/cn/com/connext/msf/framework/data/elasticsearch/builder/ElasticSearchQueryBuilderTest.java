package cn.com.connext.msf.framework.data.elasticsearch.builder;

import cn.com.connext.msf.framework.entity.ConnextEntityInfo;
import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.query.QueryOperators;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;

public class ElasticSearchQueryBuilderTest {

    private ConnextEntityInfo connextEntityInfo;

    public ElasticSearchQueryBuilderTest() {
        connextEntityInfo = new ConnextEntityInfo();
        connextEntityInfo.addField("cdp_id", "keyword", false, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("gender", "keyword", true, QueryOperators.KEYWORD_OPERATORS);
        connextEntityInfo.addField("realName", "text", true, QueryOperators.TEXT_OPERATORS);
        connextEntityInfo.addField("birthDate", "date", true, QueryOperators.DATE_OPERATORS);
    }

    @Test
    public void build() throws Exception {
        QueryInfo queryInfo = QueryInfo.from("(gender eq 男 AND birthDate ge 1980-01-01 AND birthDate lt 1990-01-01) OR gender eq 女");
        QueryBuilder queryBuilder = ElasticSearchQueryBuilder.build(connextEntityInfo, queryInfo);
        System.out.println(queryBuilder.toString());
    }

    @Test
    public void buildTest01() throws Exception {
        QueryInfo queryInfo = QueryInfo.from("birthDate le 2019-10-20");
        QueryBuilder queryBuilder = ElasticSearchQueryBuilder.build(connextEntityInfo, queryInfo);
        System.out.println(queryBuilder.toString());
    }

    @Test
    public void buildTest02() throws Exception {
        QueryInfo queryInfo = QueryInfo.from("realName eq name00000001 OR realName eq name00000002");
        QueryBuilder queryBuilder = ElasticSearchQueryBuilder.build(connextEntityInfo, queryInfo);
        System.out.println(queryBuilder.toString());
    }

    @Test
    public void buildTest03() throws Exception {
        QueryInfo queryInfo = QueryInfo.from("realName eq name00000001,name00000002");
        QueryBuilder queryBuilder = ElasticSearchQueryBuilder.build(connextEntityInfo, queryInfo);
        System.out.println(queryBuilder.toString());
    }
}