package cn.com.connext.msf.framework.utils;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorTest {

    @Test
    public void isUrlString() {
        boolean result = Validator.isUrlString("abc_123");
        Assert.assertEquals(true, result);

        result = Validator.isUrlString("ab");
        Assert.assertEquals(true, result);

        result = Validator.isUrlString("12");
        Assert.assertEquals(true, result);

        result = Validator.isUrlString("abc-123");
        Assert.assertEquals(false, result);

        result = Validator.isUrlString("a*bc-123");
        Assert.assertEquals(false, result);
    }
}