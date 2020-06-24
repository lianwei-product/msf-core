package cn.com.connext.msf.framework.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Base58UUIDTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(Base58UUIDTest.class);

    @Test
    public void newBase58UUID() throws Exception {

        long begin = System.currentTimeMillis();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            String id = Base58UUID.newBase58UUID();
            list.add(id);
            LOGGER.info(id);
        }
        long elapsedTime = System.currentTimeMillis() - begin;
        LOGGER.info("Element count: " + list.size());
        LOGGER.info("Elapsed time: " + elapsedTime + "ms.");
    }

    @Test
    public void testConvert() {
        String source = UUID.randomUUID().toString();
        LOGGER.info(source);

        String base58Id = Base58UUID.convertFromUUIDString(source);
        LOGGER.info(base58Id);

        String uuidString = Base58UUID.convertToUUID(base58Id);
        LOGGER.info(uuidString);
    }

    @Test
    public void emptyUUID() {
        String source = "00000000-0000-0000-0000-000000000000";
        String base58Id = Base58UUID.convertFromUUIDString(source);
        LOGGER.info(base58Id);

        String uuidString = Base58UUID.convertToUUID("1111111111111111");
        LOGGER.info(uuidString);
    }

}