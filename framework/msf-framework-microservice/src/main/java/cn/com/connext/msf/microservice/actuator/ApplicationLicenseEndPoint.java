package cn.com.connext.msf.microservice.actuator;

import cn.com.connext.msf.framework.auth.ApplicationLicense;
import cn.com.connext.msf.framework.auth.ApplicationLicenseProvider;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.List;

@Endpoint(id = "application-license")
public class ApplicationLicenseEndPoint {
    private final List<ApplicationLicenseProvider> applicationLicenseProviders;

    public ApplicationLicenseEndPoint(@Autowired(required = false) List<ApplicationLicenseProvider> applicationLicenseProviders) {
        this.applicationLicenseProviders = applicationLicenseProviders;
    }


    @ReadOperation
    public List<ApplicationLicense> getApplicationLicenses() {
        List<ApplicationLicense> applicationLicenses = Lists.newArrayList();
        if (applicationLicenseProviders == null) {
            return applicationLicenses;
        }

        applicationLicenseProviders.forEach(applicationLicenseProvider -> {
            applicationLicenses.add(applicationLicenseProvider.getApplicationLicense());
        });

        return applicationLicenses;
    }
}
