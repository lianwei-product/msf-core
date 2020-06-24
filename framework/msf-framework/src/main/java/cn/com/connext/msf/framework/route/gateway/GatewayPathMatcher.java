package cn.com.connext.msf.framework.route.gateway;

import org.springframework.util.AntPathMatcher;

public class GatewayPathMatcher {

    private final static AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static boolean match(String pattern, String uri) {
        return antPathMatcher.match(pattern, uri);
    }
}
