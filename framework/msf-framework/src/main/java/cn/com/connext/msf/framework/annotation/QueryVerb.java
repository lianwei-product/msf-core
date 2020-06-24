package cn.com.connext.msf.framework.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryVerb {

    @AliasFor("operator")
    String value() default "";

    @AliasFor("value")
    String operator() default "";

    boolean allowSort() default false;

}