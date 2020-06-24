package cn.com.connext.msf.data.mongo.annotation;

import cn.com.connext.msf.data.mongo.ConnextMongoRepositoryImpl;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableMongoRepositories(repositoryBaseClass = ConnextMongoRepositoryImpl.class)
public @interface EnableConnextMongoRepository {

}
