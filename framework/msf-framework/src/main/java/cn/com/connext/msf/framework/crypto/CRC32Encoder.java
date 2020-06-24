package cn.com.connext.msf.framework.crypto;

import org.springframework.util.StringUtils;

import java.util.zip.CRC32;

public class CRC32Encoder {
    public static long encode(String string) {
        if (StringUtils.isEmpty(string)) return 0;
        CRC32 crc32 = new CRC32();
        crc32.update(string.getBytes());
        return crc32.getValue();
    }

}
