package cn.com.connext.msf.framework.partition;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.config.BinderFactoryConfiguration;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;

@Configuration
@EnableConfigurationProperties({BindingServiceProperties.class})
@AutoConfigureBefore({BinderFactoryConfiguration.class})
@AutoConfigureAfter({MongoAutoConfiguration.class})
@ConditionalOnMissingBean({DynamicPartitionInfoRepository.class})
@ConditionalOnProperty(name = "mq.partition.dynamic.enable", havingValue = "true")
@Import({DynamicPartitionInfoRepository.class, DynamicPartitionService.class, KeepAliveService.class, PartitionWatchProcess.class})
public class DynamicPartitionConfig {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DynamicPartitionConfig.class);

    public DynamicPartitionConfig(DynamicPartitionService dynamicPartitionService, KeepAliveService keepAliveService) {
        init(dynamicPartitionService, keepAliveService);
    }

    private void init(DynamicPartitionService dynamicPartitionService, KeepAliveService keepAliveService) {
        log.info("init Partition Config");
        dynamicPartitionService.initPartition();
        keepAliveService.start();
    }

    @Bean("bindingService")
    @ConditionalOnMissingBean(search = SearchStrategy.CURRENT)
    public BindingService getBindingService(BindingServiceProperties bindingServiceProperties,
                                            BinderFactory binderFactory,
                                            TaskScheduler taskScheduler){
        return new ConnextBindingService(bindingServiceProperties, binderFactory, taskScheduler);
    }
}
