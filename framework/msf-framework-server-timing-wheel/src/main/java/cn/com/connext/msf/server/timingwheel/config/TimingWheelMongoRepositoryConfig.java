package cn.com.connext.msf.server.timingwheel.config;

import cn.com.connext.msf.server.timingwheel.repository.TimingWheelMongoRepository;
import cn.com.connext.msf.server.timingwheel.repository.TimingWheelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;

@Configuration
@ConditionalOnClass(MongoOperations.class)
@ConditionalOnMissingBean(TimingWheelRepository.class)
@Import({TimingWheelMongoRepository.class})
public class TimingWheelMongoRepositoryConfig {
}
