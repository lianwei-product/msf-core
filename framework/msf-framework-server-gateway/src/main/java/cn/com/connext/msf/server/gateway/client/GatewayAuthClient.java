package cn.com.connext.msf.server.gateway.client;

import cn.com.connext.msf.framework.jwt.JwtCookieInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${msf.authorization.service-name}", path = "/api/${msf.authorization.service-name}")
public interface GatewayAuthClient {

    @GetMapping("/authorization/role")
    List<String> getClientRoles(@RequestParam(value = "clientType") String clientType, @RequestParam(value = "clientId") String clientId);

    @GetMapping("/authorization/authority")
    List<String> getRoleAuthorities(@RequestParam(value = "roleId") String roleId);

    @GetMapping("/authentication/jwt_cookie_with_refresh_token")
    JwtCookieInfo refreshToken(@RequestParam(value = "jwtString") String jwtString);

    @GetMapping("/platform_client/{id}/trusted_hosts")
    String findTrustedHosts(@PathVariable String id);

    @GetMapping("/authentication/expiration")
    int getExpiration();
}

