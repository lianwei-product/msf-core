package cn.com.connext.msf.framework.jwt;

import cn.com.connext.msf.framework.crypto.RSA;
import cn.com.connext.msf.framework.utils.Base58UUID;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.util.Date;
import java.util.Map;

import static org.apache.commons.lang3.time.DateUtils.addSeconds;

public class JsonWebTokenBuilder {

    private final Logger logger = LoggerFactory.getLogger(JsonWebTokenBuilder.class);

    private final PrivateKey privateKey;
    private final int expiration;    // token 有效期，单位：秒
    private final int refreshBefore; // 在过期前指定时间刷新Token，单位：秒

    public JsonWebTokenBuilder(String privateKeyBase64, int expiration) {
        try {
            this.privateKey = RSA.getPrivateKeyFromBase64(privateKeyBase64);
            this.expiration = expiration;
            this.refreshBefore = 0;
        } catch (Exception e) {
            logger.error("Init JsonWebTokenBuilder error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public JsonWebTokenBuilder(String privateKeyBase64, int expiration, int refreshBefore) {
        try {
            this.privateKey = RSA.getPrivateKeyFromBase64(privateKeyBase64);
            this.expiration = expiration;
            this.refreshBefore = refreshBefore;
        } catch (Exception e) {
            logger.error("Init JsonWebTokenBuilder error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String buildUserToken(String userId) {
        return buildClientToken(ClientTypes.USER, null, userId);
    }

    public String buildClientToken(String clientId) {
        return buildClientToken(ClientTypes.CLIENT, null, clientId);
    }

    public String buildUserToken(String tenantId, String userId) {
        return buildClientToken(ClientTypes.USER, tenantId, userId);
    }

    public String buildClientToken(String tenantId, String clientId) {
        return buildClientToken(ClientTypes.CLIENT, tenantId, clientId);
    }

    public String buildClientToken(String subject, Map<String, Object> claims) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setId(Base58UUID.newBase58UUID());
        jwtBuilder.setSubject(subject);
        jwtBuilder.setExpiration(addSeconds(new Date(), expiration));
        claims.forEach(jwtBuilder::claim);

        jwtBuilder.signWith(SignatureAlgorithm.RS256, privateKey);
        return GlobalJwtConstant.JWT_BEARER_PREFIX + jwtBuilder.compact();
    }

    private String buildClientToken(String clientType, String tenantId, String subject) {
        Date expirationTime = addSeconds(new Date(), expiration);

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setId(Base58UUID.newBase58UUID());
        jwtBuilder.claim("clientType", clientType);
        jwtBuilder.setSubject(subject);
        jwtBuilder.setExpiration(expirationTime);

        if (clientType.equals(ClientTypes.USER) && refreshBefore > 0) {
            Date shouldRefreshTime = addSeconds(expirationTime, -1 * refreshBefore);
            jwtBuilder.claim(GlobalJwtConstant.JWT_REFRESH_BEFORE, shouldRefreshTime.toInstant().getEpochSecond());
        }

        jwtBuilder.signWith(SignatureAlgorithm.RS256, privateKey);

        if (tenantId != null) {
            jwtBuilder.claim("tenantId", tenantId);
        }

        return GlobalJwtConstant.JWT_BEARER_PREFIX + jwtBuilder.compact();
    }
}
