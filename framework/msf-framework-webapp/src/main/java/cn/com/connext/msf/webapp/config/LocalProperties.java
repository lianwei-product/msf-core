package cn.com.connext.msf.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "msf.local")
public class LocalProperties {

    private String[] basenames = new String[]{"i18n/messages"};

    public String[] getBasenames() {
        return basenames;
    }

    public void setBasenames(String[] basenames) {
        this.basenames = basenames;
    }
}
