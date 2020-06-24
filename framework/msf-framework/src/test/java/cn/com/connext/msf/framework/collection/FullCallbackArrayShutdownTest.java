package cn.com.connext.msf.framework.collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "Duplicates"})
public class FullCallbackArrayShutdownTest {
    private static final Logger logger = LoggerFactory.getLogger(FullCallbackArrayShutdownTest.class);

    @Test
    public void test() throws IOException {
        FullCallbackArray<String> array = new FullCallbackArray<>(2, (this::processArrayList));

        for (int i = 0; i < 5; i++) {
            String str = "str" + (i + 1);
            logger.info("add: " + str);
            array.add(str);
        }
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