package cn.com.connext.msf.framework.mapping;

import org.junit.Assert;
import org.junit.Test;

public class DynamicFieldMappingTypeTest {

    @Test
    public void testValues() {
        String name = DynamicFieldMappingType.CONDITION_EXP.getDynamicFieldMappingType();
        Assert.assertEquals("condition_exp", name);
    }
}