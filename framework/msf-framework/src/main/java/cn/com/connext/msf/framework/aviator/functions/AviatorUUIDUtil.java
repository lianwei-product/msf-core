package cn.com.connext.msf.framework.aviator.functions;

import cn.com.connext.msf.framework.aviator.AviatorImportFunction;
import cn.com.connext.msf.framework.utils.Base58UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @Author: ming.wang
 * @Date: 2020/11/2 0002 11:25
 * @Description:
 */
@Resource
public class AviatorUUIDUtil implements AviatorImportFunction {
    private final Logger log = LoggerFactory.getLogger(AviatorUUIDUtil.class);

    public AviatorUUIDUtil() {
        log.debug("AviatorUUIDUtil construct");
    }

    public static String uuid() {
        return Base58UUID.newBase58UUID();
    }
}

