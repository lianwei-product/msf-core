package cn.com.connext.msf.framework.piidata;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class PIIDataAESEncoderTest {

    @Test
    public void test() {
        String text = "1377065";
        if (text.length() < 7) {
            return;
        }
        System.out.println(text.substring(0, 3).concat("****").concat(text.substring(7)));

        text = "025-61402545";
        System.out.println(text.substring(0, text.length() - 4).concat("****"));

        text = "江苏省南京市";
        System.out.println(text.substring(0, 3).concat(StringUtils.leftPad("*", text.length() - 3, "*")));
    }

}