package cn.com.connext.msf.framework.collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "Duplicates"})
public class TimeCallbackArrayTest {
    private static final Logger logger = LoggerFactory.getLogger(TimeCallbackArrayTest.class);


    @Test
    public void test() throws Exception {
        TimeCallbackArray<String> array = new TimeCallbackArray<>(2, 2000, (this::processArrayList));

        for (int i = 0; i < 3; i++) {
            String str = "str" + (i + 1);
            logger.info("add: " + str);
            array.add(str);

        }

        // System.in.read();
    }

    private void processArrayList(ArrayList arrayList) {
        arrayList.forEach(tmpStr -> {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
                logger.info("out: " + tmpStr);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}