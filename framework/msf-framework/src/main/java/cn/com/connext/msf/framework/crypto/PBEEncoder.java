package cn.com.connext.msf.framework.crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.security.SecureRandom;

public class PBEEncoder {

    private final String PEE_CIPHER_ALGORITHM = "PBEWITHMD5andDES";
    private final byte[] salt = new SecureRandom().generateSeed(8);
    private Key key;

    public PBEEncoder(String password) {
        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PEE_CIPHER_ALGORITHM);
            this.key = factory.generateSecret(pbeKeySpec);
        } catch (Exception e) {
            throw new RuntimeException("PBE CreateKey error.", e);
        }
    }

    public String encode(String plainText) {
        try {
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 100);//100是你选择迭代的次数
            Cipher cipher = Cipher.getInstance(PEE_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] result = cipher.doFinal(plainText.getBytes());
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            throw new RuntimeException("PBE encode error.", e);
        }
    }

    public String decode(String encodedText) {

        try {
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 100);//100是你选择迭代的次数
            Cipher cipher = Cipher.getInstance(PEE_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            byte[] result = cipher.doFinal(Base64.decodeBase64(encodedText));
            return new String(result);
        } catch (Exception e) {
            throw new RuntimeException("PBE decode error.", e);
        }
    }
}
