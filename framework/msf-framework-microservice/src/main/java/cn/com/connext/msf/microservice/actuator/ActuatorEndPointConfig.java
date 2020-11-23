package cn.com.connext.msf.microservice.actuator;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ServiceRouteEndPoint.class, ApplicationLicenseEndPoint.class, ServiceZoneEndPoint.class})
public class ActuatorEndPointConfig {

}
