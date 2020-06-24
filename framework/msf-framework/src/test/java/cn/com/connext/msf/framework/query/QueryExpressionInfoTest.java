package cn.com.connext.msf.framework.query;

import org.junit.Assert;
import org.junit.Test;

public class QueryExpressionInfoTest {
    @Test
    public void from() throws Exception {

        QueryExpressionInfo queryExpressionInfo = QueryExpressionInfo.from("name eq abc d");
        Assert.assertEquals("name", queryExpressionInfo.getFieldName());
        Assert.assertEquals("eq", queryExpressionInfo.getOperator());
        Assert.assertEquals("abc d", queryExpressionInfo.getFieldValue());
    }

}