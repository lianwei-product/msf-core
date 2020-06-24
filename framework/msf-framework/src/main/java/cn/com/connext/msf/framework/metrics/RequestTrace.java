package cn.com.connext.msf.framework.metrics;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestTrace {

    String type() default RequestTraceTypes.INFO;

}
