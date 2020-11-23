//package cn.com.connext.msf.webapp.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.core.env.Environment;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//@ConditionalOnProperty(name = "msf.local.enable", havingValue = "true", matchIfMissing = true)
//@EnableConfigurationProperties(LocalProperties.class)
//public class LocaleConfiguration extends WebMvcConfigurerAdapter implements EnvironmentAware {
//
//    private final Logger log = LoggerFactory.getLogger(LocaleConfiguration.class);
//
//    private final LocalProperties properties;
//
//    public LocaleConfiguration(LocalProperties properties) {
//        this.properties = properties;
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        // unused
//    }
//
//    @Bean
//    public ResourceBundleMessageSource messageSource() {
//        log.info("Create messageSource...");
//        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//        String[] basenames = properties.getBasenames();
//        source.addBasenames(basenames);
//        source.setDefaultEncoding("UTF-8");
//        source.setFallbackToSystemLocale(true);
//        return source;
//    }
//
//}
