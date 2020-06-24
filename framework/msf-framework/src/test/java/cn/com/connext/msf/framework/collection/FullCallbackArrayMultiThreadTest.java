package cn.com.connext.msf.framework.collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "Duplicates"})
public class FullCallbackArrayMultiThreadTest {
    private static final Logger logger = LoggerFactory.getLogger(FullCallbackArrayMultiThreadTest.class);

    private FullCallbackArray<String> fullCallbackArray = new FullCallbackArray<>(5, (this::processArrayList));

    @Test
    public void test() throws Exception {
        int threadCount = 2;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);


        for (int i = 0; i < threadCount; i++) {
            Thread thread = new TestThread(countDownLatch);
            thread.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("test finished.");
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

    class TestThread extends Thread {
        private CountDownLatch countDownLatch;

        public TestThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                String str = Thread.currentThread().getName() + " str" + (i + 1);
                logger.info("add: " + str);
                fullCallbackArray.add(str);
            }
            countDownLatch.countDown();
        }
    }
}

