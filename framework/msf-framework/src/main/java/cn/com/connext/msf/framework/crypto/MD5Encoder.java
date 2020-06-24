package cn.com.connext.msf.framework.crypto;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by YANG205 on 2018/9/11.
 */
public class MD5Encoder {

    public static String md5(String text, String key) {
        return DigestUtils.md5Hex(text + key);
    }
}


