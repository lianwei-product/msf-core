package cn.com.connext.msf.framework.jwt;

import cn.com.connext.msf.framework.crypto.RSA;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.security.PublicKey;

public class JsonWebTokenParser {

    private final Logger logger = LoggerFactory.getLogger(JsonWebTokenParser.class);
    private final PublicKey publicKey;

    public JsonWebTokenParser(String publicKeyBase64) {
        try {
            this.publicKey = RSA.getPublicKeyFromBase64(publicKeyBase64);
        } catch (Exception e) {
            logger.error("Init JsonWebTokenParser error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取jwt中的载荷信息
     *
     * @param jwtString jwt字符串
     * @return 载荷信息，如果jwt无效或已过期，返回null.
     */
    @Nullable
    public Claims getClaims(String jwtString) {
        if (StringUtils.isEmpty(jwtString)) {
            return null;
        }

        try {
            jwtString = jwtString.replace(GlobalJwtConstant.JWT_BEARER_PREFIX, "");
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwtString).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
