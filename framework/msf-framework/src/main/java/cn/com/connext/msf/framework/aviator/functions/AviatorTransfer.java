package cn.com.connext.msf.framework.aviator.functions;

import cn.com.connext.msf.framework.aviator.AviatorImportFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: ming.wang
 * @Date: 2020/11/5 0005 14:08
 * @Description: 转换方法。 请勿重命名，牵扯表达式
 */
@Resource
public class AviatorTransfer implements AviatorImportFunction {

    private static final Logger log = LoggerFactory.getLogger(AviatorTransfer.class);

    public AviatorTransfer() {
        log.debug("AviatorTransfer construct");
    }

    public static String transfer(String src, Map<String, Map<String, String>> map) {
        try {
            if (!CollectionUtils.isEmpty(map)) {
                Double srcDou = Double.valueOf("" + src);
                for (Map.Entry<String, Map<String, String>> target : map.entrySet()) {
                    String key = target.getKey();
                    Map<String, String> value = target.getValue();
                    for (Map.Entry<String, String> child : value.entrySet()) {
                        Double start = Double.valueOf(child.getKey());
                        Double end = Double.valueOf(child.getValue());
                        if (srcDou >= start && srcDou <= end) {
                            return key;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("transfer error", e);
        }
        return "UNKNOWN";
    }

}

