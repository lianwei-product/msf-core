package cn.com.connext.msf.framework.crypto;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by YANG205 on 2018/8/28.
 */
public class AESEncoder {

    private final SecretKey secretKey;
    private final String ECB_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";


    public AESEncoder(String accessKey) {
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(accessKey), "AES");
    }


    public String encode(String plainText) {
        if (StringUtils.isBlank(plainText)) {
            return "";
        }

        try {
            Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = plainText.getBytes("UTF-8");
            return Base64.getEncoder().encodeToString(cipher.doFinal(bytes));
        } catch (Exception e) {
            throw new RuntimeException("AES encode error.", e);
        }
    }


    public String decode(String encodedText) {
        if (StringUtils.isBlank(encodedText)) {
            return "";
        }

        try {
            Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] bytes = Base64.getDecoder().decode(encodedText);
            return new String(cipher.doFinal(bytes), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("AES decode error.", e);
        }
    }

}
