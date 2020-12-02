package cn.com.connext.msf.apimetrics.filter;

import com.netflix.zuul.ZuulFilter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(ZuulFilter.class)
@Import({ZuulPreFilter.class, ZuulPostFilter.class})
public class ZuulFilterConfig {

    public ZuulFilterConfig() {
        LoggerFactory.getLogger(ZuulFilterConfig.class).info("Init api metrics zuul filter config...");
    }
}
