package cn.com.connext.msf.framework.crypto;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import static org.hamcrest.core.Is.is;


public class RSATest {

    private final Logger logger = LoggerFactory.getLogger(RSATest.class);

    @Test
    public void getKey() throws Exception {
        KeyPair keyPair = RSA.getKey(512);
        String publicBase64 = RSA.getBase64FromKey(keyPair.getPublic());
        String privateBase64 = RSA.getBase64FromKey(keyPair.getPrivate());

        logger.info("Public Key: " + publicBase64);
        logger.info("Private Key: " + privateBase64);

        PublicKey publicKey = RSA.getPublicKeyFromBase64(publicBase64);
        PrivateKey privateKey = RSA.getPrivateKeyFromBase64(privateBase64);

        String encryptMessage = "Test Message.";
        String decryptMessage = null;

        byte[] encryptBytes = RSA.encryptByPrivateKey(encryptMessage.getBytes(), privateKey.getEncoded());
        byte[] decryptBytes = RSA.decryptByPublicKey(encryptBytes, publicKey.getEncoded());

        decryptMessage = new String(decryptBytes);

        logger.info("encryptMessage:" + encryptMessage);
        logger.info("decryptMessage:" + decryptMessage);

        Assert.assertThat(decryptMessage, is(encryptMessage));

    }

    @Test
    public void timeTest() {
        logger.info("01:" + System.currentTimeMillis());
        logger.info("02:" + new Date().getTime());
    }

}