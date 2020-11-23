package cn.com.connext.msf.microservice.feign;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.jwt.GlobalJwtConstant;
import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.webapp.config.WebApplicationExceptionMessage;
import com.google.common.collect.Maps;
import feign.RequestInterceptor;
import feign.Response;
import feign.Util;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Map;

@Configuration
public class FeignClientConfig implements ErrorDecoder {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public FeignClientConfig(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> {
            if (RequestContextHolder.getRequestAttributes() == null) {
                return;
            }

            ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            if (servletRequestAttributes == null) {
                return;
            }

            HttpServletRequest request = servletRequestAttributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (filterJWTHeaders(name)) {
                        template.header(name, values);
                    }
                }
            }
        };
    }


    @Bean
    public Encoder feignEncoder() {
        return new FeignClientCustomEncoder(new SpringEncoder(messageConverters));
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = getResponseBody(response);
        // msf 框架约定，上游服务抛出业务异常时，Http状态码为400
        if (response.status() == 400) {
            WebApplicationExceptionMessage exceptionMessage = getExceptionMessage(body);
            if (exceptionMessage != null) {
                return new BusinessException(exceptionMessage.getCode(), exceptionMessage.getMessage());
            }
        }

        Map<String, Object> requestInfo = Maps.newLinkedHashMap();
        requestInfo.put("status", response.status());
        requestInfo.put("method", response.request().method());
        requestInfo.put("url", response.request().url());
        requestInfo.put("response", body);

        return new RuntimeException(MessageFormat.format("Run {0} error:\n{1}", methodKey, JSON.toIndentJsonString(requestInfo)));
    }

    private String getResponseBody(Response response) {
        try {
            return Util.toString(response.body().asReader());
        } catch (IOException e) {
            return null;
        }
    }

    private WebApplicationExceptionMessage getExceptionMessage(String body) {
        if (StringUtils.isEmpty(body)) {
            return null;
        }

        try {
            return JSON.parseObject(body, WebApplicationExceptionMessage.class);
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean filterJWTHeaders(String name) {
        return name.equals(GlobalJwtConstant.JWT_HEADER_CLIENT_Id)
                || name.equals(GlobalJwtConstant.JWT_HEADER_CLIENT_TYPE)
                || name.equals(GlobalJwtConstant.JWT_HEADER_TENANT_ID);

    }
}
