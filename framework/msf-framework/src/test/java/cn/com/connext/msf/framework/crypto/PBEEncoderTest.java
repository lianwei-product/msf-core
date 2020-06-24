package cn.com.connext.msf.framework.crypto;

import org.junit.Test;

public class PBEEncoderTest {

    @Test
    public void jdkPBE2() throws Exception {
        String password = "key_password2";
        PBEEncoder pbeEncoder = new PBEEncoder(password);
        String context = pbeEncoder.encode("hello word");
        System.out.println(context);
        System.out.println(pbeEncoder.decode(context));
    }

}
