package cn.com.connext.msf.framework.piidata;

import cn.com.connext.msf.framework.crypto.AESEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class PIIDataAESEncoder implements PIIDataEncoder {

    private final Logger logger = LoggerFactory.getLogger(PIIDataAESEncoder.class);
    private final AESEncoder aesEncoder;

    public PIIDataAESEncoder(@Value("${pii-data.secret-key}") String secretKey) {
        this.aesEncoder = new AESEncoder(secretKey);
    }

    @Override
    public String encode(String plainText) {
        return aesEncoder.encode(plainText);
    }

    @Override
    public String decode(String encodedText) {
        try {
            return aesEncoder.decode(encodedText);
        } catch (Exception e) {
            logger.error("decode error.", e);
            return encodedText;
        }
    }

    @Override
    public String decodeMobile(String encodedText) {
        String text = decode(encodedText);
        if (StringUtils.isBlank(text)) {
            return text;
        }

        if (text.length() < 7) {
            return text;
        }

        return text.substring(0, 3).concat("****").concat(text.substring(7));
    }

    @Override
    public String decodePhone(String encodedText) {
        String text = decode(encodedText);
        if (StringUtils.isBlank(text)) {
            return text;
        }

        if (text.length() < 8) {
            return text;
        }

        return text.substring(0, text.length() - 4).concat("****");
    }

    @Override
    public String decodeAddress(String encodedText) {
        String text = decode(encodedText);
        if (StringUtils.isBlank(text)) {
            return text;
        }

        if (text.length() < 4) {
            return text;
        }

        return text.substring(0, 3).concat(StringUtils.leftPad("*", text.length() - 3, "*"));
    }

}
