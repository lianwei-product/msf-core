package cn.com.connext.msf.framework.distributeid.config;

import cn.com.connext.msf.framework.distributeid.provider.ClusterNodeNameProvider;
import cn.com.connext.msf.framework.distributeid.provider.MongoClusterNodeNameProvider;
import cn.com.connext.msf.framework.distributeid.repository.ClusterNodeRenewInfoRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnMissingBean(ClusterNodeNameProvider.class)
@Import({MongoClusterNodeNameProvider.class, ClusterNodeRenewInfoRepository.class})
public class DefaultClusterNodeNameProviderConfig {

    public DefaultClusterNodeNameProviderConfig() {
        LoggerFactory.getLogger(DefaultClusterNodeNameProviderConfig.class).info("Init DistributeIdGenerator by MongoClusterNodeNameProvider.");
    }
}

