package cn.com.connext.msf.framework.jwt;

import javax.servlet.http.Cookie;
import java.net.URLEncoder;


public class JsonWebTokenCookieBuilder {

    public static Cookie buildCookie(String domain, String jwtToken, boolean isSecure) {
        jwtToken = encodeToken(jwtToken);
        Cookie cookie = new Cookie(GlobalJwtConstant.COOKIE_TOKEN_NAME, jwtToken);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(isSecure);
        return cookie;
    }

    private static String encodeToken(String jwtToken) {
        if (jwtToken == null) {
            return null;
        }

        try {
            jwtToken = URLEncoder.encode(jwtToken, "UTF-8");
        } catch (Exception e) {
            jwtToken = null;
        }
        return jwtToken;
    }
}
