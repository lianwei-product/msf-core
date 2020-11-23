package cn.com.connext.msf.microservice.annotation;

import cn.com.connext.msf.webapp.annotation.WebApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@WebApplication
@EnableDiscoveryClient
@EnableFeignClients
public @interface MicroServiceApplication {

}
