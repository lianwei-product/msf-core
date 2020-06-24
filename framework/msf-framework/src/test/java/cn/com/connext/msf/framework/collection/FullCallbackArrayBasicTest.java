package cn.com.connext.msf.framework.collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class FullCallbackArrayBasicTest {
    private static final Logger logger = LoggerFactory.getLogger(FullCallbackArrayBasicTest.class);

    @Test
    public void testByLambda() throws IOException {
        FullCallbackArray<String> array = new FullCallbackArray<>(5, (this::processArrayList));

        for (int i = 0; i < 10; i++) {
            String str = "str" + (i + 1);
            logger.info("add: " + str);
            array.add(str);
        }
    }

    private void processArrayList(ArrayList arrayList) {
        arrayList.forEach(tmpStr -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                logger.info("out: " + tmpStr);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    public void testByInnerClass() {
        FullCallbackArray<String> array = new FullCallbackArray<>(5, new FullCallback()::process);
        for (int i = 0; i < 10; i++) {
            String str = "str" + (i + 1);
            logger.info("add: " + str);
            array.add(str);
        }
    }

    public class FullCallback implements ArrayCallback<String> {
        @Override
        public void process(ArrayList arrayList) {
            processArrayList(arrayList);
        }
    }


}