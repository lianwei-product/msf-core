package cn.com.connext.msf.server.timingwheel.config;

import cn.com.connext.msf.server.timingwheel.service.TimingWheelService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TimingWheelBootstrapper.class,
        TimingWheelService.class
})
public class TimingWheelBootstrapperConfig {
}
