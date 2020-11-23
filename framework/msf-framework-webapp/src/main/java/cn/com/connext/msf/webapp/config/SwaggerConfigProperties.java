package cn.com.connext.msf.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfigProperties {

    private String appName;
    private String description;
    private String apiPackage;


    public SwaggerConfigProperties(
            @Value("${msf.application.name}") String appName,
            @Value("${msf.application.description}") String description,
            @Value("${msf.application.apiPackage}") String apiPackage
    ) {
        this.appName = appName;
        this.description = description;
        this.apiPackage = apiPackage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiPackage() {
        return apiPackage;
    }

    public void setApiPackage(String apiPackage) {
        this.apiPackage = apiPackage;
    }
}
