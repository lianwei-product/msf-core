package cn.com.connext.msf.server.registry.annotation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableEurekaServer
@SpringBootApplication
public @interface RegistryServerApplication {
}
