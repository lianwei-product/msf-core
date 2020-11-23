package cn.com.connext.msf.server.management.annotation;

import cn.com.connext.msf.data.mongo.ConnextMongoRepositoryImpl;
import cn.com.connext.msf.microservice.annotation.MicroServiceApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@MicroServiceApplication
@EnableMongoRepositories(repositoryBaseClass = ConnextMongoRepositoryImpl.class, basePackages = {"cn.com.connext.msf.server.management.repository"})
public @interface ManagementServerApplication {

}
