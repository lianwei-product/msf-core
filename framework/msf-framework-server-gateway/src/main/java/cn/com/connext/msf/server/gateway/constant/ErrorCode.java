package cn.com.connext.msf.server.gateway.constant;

import com.google.common.collect.Maps;

import java.util.Map;

public class ErrorCode {

    private final static Map<Integer, String> MESSAGE_MAP;

    static {
        MESSAGE_MAP = Maps.newHashMap();
        MESSAGE_MAP.put(400, "{\"code\":\"400\",\"message\":\"Request method does not supported.\"}");
        MESSAGE_MAP.put(401, "{\"code\":\"401\",\"message\":\"Need authorization.\"}");
        MESSAGE_MAP.put(403, "{\"code\":\"403\",\"message\":\"Do not have authority.\"}");
        MESSAGE_MAP.put(404, "{\"code\":\"404\",\"message\":\"Request url not found.\"}");
        MESSAGE_MAP.put(407, "{\"code\":\"407\",\"message\":\"Gateway server on initializing, please wait.\"}");
    }

    public static String get(int code) {
        return MESSAGE_MAP.get(code);
    }
}
