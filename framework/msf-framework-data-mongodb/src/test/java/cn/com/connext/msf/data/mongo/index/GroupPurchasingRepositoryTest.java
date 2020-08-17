package cn.com.connext.msf.data.mongo.index;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.data.mongo.ConnextMongoRepositoryBuilder;
import cn.com.connext.msf.framework.utils.Base58UUID;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class GroupPurchasingRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(GroupPurchasingRepositoryTest.class);
    private final GroupPurchasingRepository repository;
    private final int testCount = 100 * 10000;

    private final Random random = new Random();

    public GroupPurchasingRepositoryTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.INFO);
        loggerContext.getLogger("org.mongodb").setLevel(Level.WARN);

        repository = ConnextMongoRepositoryBuilder.build(GroupPurchasingRepository.class);


    }

    @Test
    public void create() {
        List<GroupPurchasing> records = Lists.newArrayList();
        for (int i = 0; i < testCount; i++) {
            GroupPurchasing groupPurchasing = new GroupPurchasing();
            groupPurchasing.setId(Base58UUID.newBase58UUID());
            groupPurchasing.setActivityId("activity" + (random.nextInt(10) + 1));
            List<String> customers = Lists.newArrayList();
            for (int c = 0; c < 10; c++) {
                String customerId = "customer" + (random.nextInt(100) + 1);
                if(!customers.contains(customerId)){
                    customers.add(customerId);
                }
            }
            groupPurchasing.setCustomers(customers);
            groupPurchasing.setUpdatedTime(new Date());

            records.add(groupPurchasing);
        }

        long begin = System.currentTimeMillis();
        repository.batchCreate(records, 5000);

        logger.info("Create time took: {}ms, record count: {}", System.currentTimeMillis() - begin, testCount);
    }
}