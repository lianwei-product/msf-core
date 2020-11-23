package cn.com.connext.msf.webapp.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAuthority {

    /**
     * 权限名称
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 权限名称
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 无需鉴权，仅用于需要直接对外暴露的接口
     */
    boolean noAuth() default false;
}
