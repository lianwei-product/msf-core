package cn.com.connext.msf.framework.jwt;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class JsonWebTokenBuilderTest {

    private final Logger logger = LoggerFactory.getLogger(JsonWebTokenBuilderTest.class);

    private final String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAlkzIHlE01SWMshUyS7vxd3JNnZGdW5TuGitNknGBOPhfnHT0IevV_Z7idrQcNVIMUbrK54mYL5Dkz58GzTZonQIDAQABAkAS6A20Ypq2xUZxkGEHTdkOIX7J92tH_fAFsrTLWyPaOpI7OX7Fbz-YhXZ6CO3IXHH_wTTeA__p3ad_2T-oVpgBAiEA5PwYqBHdLm0gr48GlhFj2OGMO10cmIwGOPdxlGPaZIECIQCoCDW6xLjqpVPfsugIhb1vzH7oAOWMUAXYwymcmB4GHQIgH0SBRWnzDuTd4rsgBhvny3S5Bl4ninkMXHkFbGLaLoECIGBvNcbWl_myoIxFtP0PJEVBa6Piv9rjIjfg6cTBWJnRAiEAqV36-xWAmpbXVxiCaEQqg8cYKdhEAOQRkjpnZvG3RWw";
    private final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJZMyB5RNNUljLIVMku78XdyTZ2RnVuU7horTZJxgTj4X5x09CHr1f2e4na0HDVSDFG6yueJmC-Q5M-fBs02aJ0CAwEAAQ";

    @Test
    public void test() {

        JsonWebTokenBuilder jsonWebTokenBuilder = new JsonWebTokenBuilder(privateKey, 7200);
        Map<String, Object> claimsMap = Maps.newLinkedHashMap();
        claimsMap.put("tenantId", "t01");
        claimsMap.put("buCode", "b01");
        claimsMap.put("isMember", false);

        String token = jsonWebTokenBuilder.buildClientToken("member01", claimsMap);
        logger.info("JwtToken: {}", token);

        JsonWebTokenParser jsonWebTokenParser = new JsonWebTokenParser(publicKey);
        Claims claims = jsonWebTokenParser.getClaims(token);
        if (claims == null) {
            return;
        }

        claims.forEach((key, value) -> {
            logger.info("key: {}, value: {}", "jwt_".concat(key), value);
        });

        logger.info("expire time: {}", new Date(Long.parseLong(claims.get("exp").toString()) * 1000));

    }

}