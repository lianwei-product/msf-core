package cn.com.connext.msf.data.mongo.config;

import cn.com.connext.msf.data.mongo.ConnextMongoOperationManager;
import cn.com.connext.msf.data.mongo.ConnextShardRepositoryManager;
import cn.com.connext.msf.data.mongo.converter.BigDecimalConverter;
import cn.com.connext.msf.data.mongo.converter.JSR310DateConverter;
import cn.com.connext.msf.data.mongo.provider.MongoOperationsProvider;
import cn.com.connext.msf.data.mongo.provider.SimpleMongoOperationsProvider;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
@Import(ConnextShardRepositoryManager.class)
public class MongoConfig {

    private final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    @Bean
    @ConditionalOnMissingBean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Lists.newArrayList(
                JSR310DateConverter.DateToZonedDateTimeConverter.INSTANCE,
                JSR310DateConverter.ZonedDateTimeToDateConverter.INSTANCE,
                JSR310DateConverter.LocalDateTimeToDateConverter.INSTANCE,
                JSR310DateConverter.DateToLocalDateConverter.INSTANCE,
                BigDecimalConverter.Decimal128ToBigDecimalConverter.INSTANCE,
                BigDecimalConverter.BigDecimalToDecimal128Converter.INSTANCE
        ));
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(MongoOperations.class)
    public MongoOperationsProvider simpleMongoOperationsProvider(MongoOperations mongoOperations) {
        logger.info("Init MongoOperationsProvider by SimpleMongoOperationsProvider");
        return new SimpleMongoOperationsProvider(mongoOperations);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnextMongoOperationManager connextMongoOperationManager(MongoCustomConversions mongoCustomConversions) {
        return new ConnextMongoOperationManager(mongoCustomConversions);
    }
}
