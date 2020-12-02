package cn.com.connext.msf.framework.jwt;

public class JwtCookieInfo {

    private String domain;
    private String jwtToken;

    public static JwtCookieInfo from(String domain, String jwtToken) {
        JwtCookieInfo jwtCookieInfo = new JwtCookieInfo();
        jwtCookieInfo.domain = domain;
        jwtCookieInfo.jwtToken = jwtToken;
        return jwtCookieInfo;
    }

    public String getDomain() {
        return domain;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
