package utils;

import cn.com.connext.msf.framework.saas.notify.utils.DingtalkSecretEncode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DingtalkSecretEncodeTest {

    private final Logger logger = LoggerFactory.getLogger(DingtalkSecretEncodeTest.class);
    @Test
    public void encodeSecret() {
        Long timestamp = System.currentTimeMillis();
        String sign= DingtalkSecretEncode.encodeSecret("SEC0c7e85710ba4a190578106a69b6ebfc82edca0f3142767ec1841866b032539b5",timestamp);
        logger.info(sign);
        logger.info(timestamp.toString());
    }
}
