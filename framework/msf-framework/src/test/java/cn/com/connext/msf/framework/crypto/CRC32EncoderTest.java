package cn.com.connext.msf.framework.crypto;

import org.junit.Test;

public class CRC32EncoderTest {
    @Test
    public void encode() throws Exception {
        String string = "Table 'saas-common-ascode.connext_brand' doesn't exist";
        long result = CRC32Encoder.encode(string);
        System.out.println(result);

        string = "";
        result = CRC32Encoder.encode(string);
        System.out.println(result);

        string = null;
        result = CRC32Encoder.encode(string);
        System.out.println(result);
    }

}