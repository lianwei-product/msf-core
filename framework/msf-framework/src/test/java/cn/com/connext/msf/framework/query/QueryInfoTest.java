package cn.com.connext.msf.framework.query;

import org.junit.Assert;
import org.junit.Test;

public class QueryInfoTest {
    @Test
    public void from() throws Exception {
        QueryInfo queryInfo = QueryInfo.from("level eq {0} AND score gt {1}", "abc", 30000);
        Assert.assertEquals("level eq abc AND score gt 30000", queryInfo.getExpression());
    }

}