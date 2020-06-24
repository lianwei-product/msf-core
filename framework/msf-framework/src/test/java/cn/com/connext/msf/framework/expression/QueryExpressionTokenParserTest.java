package cn.com.connext.msf.framework.expression;

import cn.com.connext.msf.framework.query.QueryExpression;
import cn.com.connext.msf.framework.utils.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QueryExpressionTokenParserTest {

    private Logger logger = LoggerFactory.getLogger(QueryExpressionTokenParserTest.class);

    @Test
    public void testParse() {
        String queryString = "gender eq male";
        List<QueryExpression> expressions = QueryExpression.parse(queryString);
        logger.debug(JSON.toIndentJsonString(expressions));
        Assert.assertEquals(1, expressions.size());
    }

    @Test
    public void testParseAnd() {
        String queryString = "gender eq male AND age ge 40 AND age lt 50";
        List<QueryExpression> expressions = QueryExpression.parse(queryString);
        logger.debug(JSON.toIndentJsonString(expressions));
        Assert.assertEquals(3, expressions.size());
    }

    @Test
    public void testParseOR() {
        String queryString = "(gender eq male AND age ge 40) OR age lt 50";
        List<QueryExpression> expressions = QueryExpression.parse(queryString);
        logger.debug(JSON.toIndentJsonString(expressions));

        Exception e = null;
        try {
            queryString = "gender eq male AND age ge 40 OR age lt 50";
            expressions = QueryExpression.parse(queryString);
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNotEquals(null, e);
    }


    @Test
    public void test02() {
        String queryString = "(gender eq male AND (age ge 40 AND age lt 50)) OR (gender eq female AND age gt 30)";
        System.out.println(JSON.toJsonString(QueryExpression.parse(queryString)));

        queryString = "gender eq male AND age ge 40 AND age lt 50";
        System.out.println(JSON.toJsonString(QueryExpression.parse(queryString)));
    }

    @Test
    public void performanceTest() {
        long beginTime = System.currentTimeMillis();
        for (int n = 0; n < 10000; n++) {
            String queryString = "(gender eq   male    AND (age ge 40 AND age lt 50)) OR (gender eq female AND age gt 30)";
            QueryExpression.parse(queryString);
        }
        System.out.println(System.currentTimeMillis() - beginTime);
    }

    @Test
    public void test04() {
        String queryString = "gender eq male  AND age ge 40 AND age lt 50";
        System.out.println(JSON.toJsonString(QueryExpression.parse(queryString)));
    }

    @Test
    public void testParse05() {
        String queryString = "gender eq female AND (age le 14 OR age ge 70)";
        List<QueryExpression> expressions = QueryExpression.parse(queryString);
        logger.debug(JSON.toIndentJsonString(expressions));
    }

    @Test
    public void testParse06() {
        String queryString = "fileName eq test%20%281%29.txt";
        List<QueryExpression> expressions = QueryExpression.parse(queryString);
        logger.debug(JSON.toIndentJsonString(expressions));
    }

}