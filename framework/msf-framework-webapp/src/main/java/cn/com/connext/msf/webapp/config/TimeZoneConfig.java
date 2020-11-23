package cn.com.connext.msf.webapp.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
@ConditionalOnProperty(name = "msf.default-time-zone", matchIfMissing = false)
public class TimeZoneConfig {

    public TimeZoneConfig(@Value("${msf.default-time-zone}") String defaultTimeZone) {
        TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZone));
        LoggerFactory.getLogger(getClass()).info("Init default timezone: {}", defaultTimeZone);
    }

}
