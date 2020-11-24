package cn.com.connext.msf.data.jpa.annotation;

import cn.com.connext.msf.data.jpa.ConnextJpaRepositoryImpl;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableJpaRepositories(repositoryBaseClass = ConnextJpaRepositoryImpl.class)
public @interface EnableConnextJpaRepository {

}
