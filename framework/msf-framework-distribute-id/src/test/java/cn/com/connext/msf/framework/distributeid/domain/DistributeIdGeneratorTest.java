package cn.com.connext.msf.framework.distributeid.domain;

import cn.com.connext.msf.framework.distributeid.provider.ClusterNodeNameProvider;
import cn.com.connext.msf.framework.utils.Time;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;


public class DistributeIdGeneratorTest {

    private final Logger logger = LoggerFactory.getLogger(DistributeIdGeneratorTest.class);

    @Test
    public void printDayId() {
        ClusterNodeNameProvider clusterNodeNameProvider = new MemoryClusterNodeNameProvider();
        DistributeIdGenerator distributeIdGenerator = new DistributeIdGenerator(7, 0, "yyMMdd", clusterNodeNameProvider);
        String id = distributeIdGenerator.newId("store01", "BrandA");
        logger.info("id: {}, length: {}", id, id.length());
    }

    @Test
    public void printId() throws InterruptedException {
        ClusterNodeNameProvider clusterNodeNameProvider = new MemoryClusterNodeNameProvider();
        DistributeIdGenerator distributeIdGenerator = new DistributeIdGenerator(6, 2, "yyMMddHHmmss", clusterNodeNameProvider);
        for (int i = 0; i < 20; i++) {
            if (i % 10 == 0) {
                Thread.sleep(1000);
            }
            String id = distributeIdGenerator.newSeparateId("audi_order", "AUDI_O_");
            logger.info(id);
        }
    }

    @Test
    public void performance() {
        ClusterNodeNameProvider clusterNodeNameProvider = new MemoryClusterNodeNameProvider();
        DistributeIdGenerator distributeIdGenerator = new DistributeIdGenerator(6, 0, "yyMMdd", clusterNodeNameProvider);
        long beginTime = System.currentTimeMillis();
        String id = "";
        for (int i = 0; i < 100 * 10000; i++) {
            id = distributeIdGenerator.newSeparateId("audi_order", "AUDI_O_");
        }
        logger.info("last id:{}, time took：{}ms", id, System.currentTimeMillis() - beginTime);
    }

    @Test
    public void performanceParallelMode() {
        ClusterNodeNameProvider clusterNodeNameProvider = new MemoryClusterNodeNameProvider();
        DistributeIdGenerator distributeIdGenerator = new DistributeIdGenerator(6, 2, "yyMMddHHmmss", clusterNodeNameProvider);
        long beginTime = System.currentTimeMillis();
        AtomicReference<String> id = new AtomicReference<>();
        IntStream.range(0, 100 * 10000).parallel().forEach(i -> {
            id.set(distributeIdGenerator.newSeparateId("audi_order", "AUDI_O_"));
        });
        logger.info("last id:{}, time took：{}ms", id.get(), System.currentTimeMillis() - beginTime);
    }

    @Test
    public void performanceParallelModeAndLog() {
        ClusterNodeNameProvider clusterNodeNameProvider = new MemoryClusterNodeNameProvider();
        DistributeIdGenerator distributeIdGenerator = new DistributeIdGenerator(6, 2, "yyMMddHHmmss", clusterNodeNameProvider);
        long beginTime = System.currentTimeMillis();
        IntStream.range(0, 100).parallel().forEach(i -> {
            String id = distributeIdGenerator.newSeparateId("audi_order", "AUDI_O_");
            System.out.println(id);
        });
        logger.info("time took：{}ms", System.currentTimeMillis() - beginTime);
    }

    @Test
    public void testExpireTime() {
        long currentTime = Instant.now().toEpochMilli();
        Date current = new Date(currentTime);
        logger.info("current: {}", Time.getTimeFormatString(current));

        long expireTime = currentTime / 1000;
        Date expire = new Date(expireTime * 1000);
        logger.info("expire by SECONDS: {}", Time.getTimeFormatString(expire));

        expireTime = currentTime / 60000;
        expire = new Date(expireTime * 60000);
        logger.info("expire by MINUTES: {}", Time.getTimeFormatString(expire));

        expireTime = currentTime / 3600000;
        expire = new Date(expireTime * 3600000);
        logger.info("expire by HOURS: {}", Time.getTimeFormatString(expire));

        expireTime = currentTime / 86400000;
        expire = new Date(expireTime * 86400000);
        logger.info("expire by DAY: {}", Time.getTimeFormatString(expire));
    }


}