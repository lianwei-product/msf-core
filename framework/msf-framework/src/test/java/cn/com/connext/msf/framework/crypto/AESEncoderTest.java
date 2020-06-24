package cn.com.connext.msf.framework.crypto;

import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class AESEncoderTest {
    @Test
    public void encode() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); //192,256
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] bytes = secretKey.getEncoded();

        System.out.println(Base64.getEncoder().encodeToString(bytes));

        AESEncoder aesEncoder = new AESEncoder("gL9WAJrYGxrZd0w9vhqolw==");
        String encodeString = aesEncoder.encode(null);
        System.out.println(encodeString);
        System.out.println(aesEncoder.decode(encodeString));
    }

    @Test
    public void benchmark() {
        AESEncoder aesEncoder = new AESEncoder("AIDpVogUL3zQ9t6AM9uAEw==");

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            String encodeString = aesEncoder.encode("13770655999");
            String decodeString = aesEncoder.decode(encodeString);
        }

        System.out.println("time took: " + (System.currentTimeMillis() - begin));
    }
}