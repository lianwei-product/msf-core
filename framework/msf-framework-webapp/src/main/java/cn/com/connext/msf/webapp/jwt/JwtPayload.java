package cn.com.connext.msf.webapp.jwt;

import cn.com.connext.msf.framework.jwt.GlobalJwtConstant;
import cn.com.connext.msf.framework.utils.Validator;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

public class JwtPayload {


    public static String getTenantId() {
        return get(GlobalJwtConstant.JWT_HEADER_TENANT_ID);
    }

    public static String getClientId() {
        return get(GlobalJwtConstant.JWT_HEADER_CLIENT_Id);
    }

    public static String getClientType() {
        return get(GlobalJwtConstant.JWT_HEADER_CLIENT_TYPE);
    }

    public static boolean externalAccess() {
        return !StringUtils.isEmpty(get(GlobalJwtConstant.EXTERNAL_ACCESS_HEADER));
    }

    public static String getUserId() {
        return getClientId();
    }

    public static String get(String name) {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request.getHeader(name);
    }


}
