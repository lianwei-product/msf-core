package cn.com.connext.msf.framework.utils;

import org.slf4j.Logger;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动信息类
 */
public class BootingInfo {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BootingInfo.class);

    public static void printBootingInfo(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        try {
            log.info("\n----------------------------------------------------------\n\t" +
                            "Application '{}' is running! Access URLs:\n\t" +
                            "Local: \t\t{}://localhost:{}\n\t" +
                            "External: \t{}://{}:{}\n\t" +
                            "Profile(s): \t{}\n----------------------------------------------------------",
                    env.getProperty("spring.application.name"),
                    protocol,
                    env.getProperty("server.port"),
                    protocol,
                    InetAddress.getLocalHost().getHostAddress(),
                    env.getProperty("server.port"),
                    env.getActiveProfiles());
        } catch (UnknownHostException e) {
            log.error("Booting has exception!", e);
            throw new RuntimeException(e);
        }
        if (env.getProperty("spring.cloud.config.enabled", Boolean.class)) {
            String configServerStatus = env.getProperty("CONFIG_GIT_URL");
            log.info("\n----------------------------------------------------------\n\t" +
                            "Config Server: \t{}\n----------------------------------------------------------",
                    configServerStatus == null ? "Not found or not setup for this application" : configServerStatus);
        } else {
            log.info("\n----------------------------------------------------------\n\t" +
                    "Config Server: \tLocal Config\n----------------------------------------------------------");
        }
    }
}
