package cn.com.connext.msf.framework.metrics;

import org.springframework.util.AntPathMatcher;

public class RequestPathMatcher {

    private final static AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static boolean match(String pattern, String uri) {
        return antPathMatcher.match(pattern, uri);
    }
}
