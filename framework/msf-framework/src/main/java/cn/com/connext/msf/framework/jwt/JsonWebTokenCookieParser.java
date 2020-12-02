package cn.com.connext.msf.framework.jwt;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class JsonWebTokenCookieParser {

    public static String parse(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(GlobalJwtConstant.COOKIE_TOKEN_NAME)) {
                try {
                    return URLDecoder.decode(cookie.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        }

        return null;
    }
}
