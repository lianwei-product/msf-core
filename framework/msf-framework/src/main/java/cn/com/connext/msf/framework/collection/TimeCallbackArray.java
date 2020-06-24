package cn.com.connext.msf.framework.collection;

import cn.com.connext.msf.framework.concurrent.TimeWaiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 对象名称：定时回调集合
 * 开发人员：程瀚
 * 修订日期：2018-03-22 12:59:44
 * 摘要说明：该集合类在初始化时，需要指定集合的最大容量、回调执行间隔及回调方法，可以通过add方法向
 * 集合中添加元素，当所添加的元素达到集合的最大容量时，会自动触发回调方法，同时在回调方法完成前，所
 * 有对add方法的调用都将被阻塞，直至回调方法执行完成。如果在回调执行间隔时间到达时，
 * <p>
 * 该集合在初始化时，默认会注册应用退出事件，在应用通过正常方式退出时（如kill或kill -15），
 * 如果集合中仍有元素，会确保再次触发回调方法，并保证在其执行完成后，应用才会真正退出。
 * <p>
 * 该集合类是线程安全的，可以在多个线程中，同时执行add方法添加元素。
 */
@SuppressWarnings({"Duplicates"})
public class TimeCallbackArray<E> {

    private static final Logger logger = LoggerFactory.getLogger(TimeCallbackArray.class);

    private ArrayList<E> arrayList;
    private int capacity;
    private ArrayCallback<E> arrayCallback;
    private Semaphore semaphore;
    private int period;
    private TimeWaiter timeWaiter;

    /**
     * 默认实例化方法
     *
     * @param capacity      集合的最大容量，必须大于0。
     * @param period        定时执行间隔，单位毫秒
     * @param arrayCallback 当集合达到最大容量时，所要执行的回调方法。
     */
    public TimeCallbackArray(int capacity, int period, ArrayCallback<E> arrayCallback) {
        if (capacity < 1) throw new RuntimeException("array capacity must more then one.");
        if (period < 1) throw new RuntimeException("callback period must more then one.");

        this.capacity = capacity;               // set array max capacity
        this.arrayCallback = arrayCallback;
        this.arrayList = new ArrayList<>();
        this.semaphore = new Semaphore(1, true);
        this.period = period;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Thread.currentThread().setName("ShutdownHook");
            logger.info("receive application shutdown signal.");

            try {
                semaphore.acquire();
            } catch (InterruptedException ex) {
                logger.error("shutdown application error.", ex);
            }

            if (arrayList.size() == 0) {
                logger.info("array is empty, application will shutdown safely.");
                return;
            }

            logger.info("array is not empty, start callback method.");
            invokeCallback();
            logger.info("callback complete, application will shutdown safely.");
        }));

        Thread thread = new Thread(() -> {
            timeWaiter = new TimeWaiter();
            for (; ; ) {
                timeWaiter.wait(period, TimeUnit.MILLISECONDS);
                semaphore.acquireUninterruptibly();
                logger.info("TimeWaiter callback");
                invokeCallback();
                semaphore.release();
            }
        });
        thread.setName("TimeWaiter");
        thread.start();
    }

    /**
     * 向集合中添加元素，当所添加的元素达到集合的最大容量时，会自动触发回调方法，同时在回调方法完成前，
     * 所有对add方法的调用都将被阻塞，直至回调方法执行完成。
     *
     * @param e 所要添加的元素
     */
    public void add(E e) {
        try {
            semaphore.acquire();
            // 检测是否将要达到集合的最大容量
            if (arrayList.size() < capacity - 1) {
                arrayList.add(e);
            } else {
                logger.info("array is full, start callback method.");
                arrayList.add(e);
                timeWaiter.pause();
                invokeCallback();
                timeWaiter.reset();
            }
            semaphore.release();
        } catch (Exception ex) {
            logger.error("add element to array error.", ex);
        }
    }

    private void invokeCallback() {
        if (arrayList.size() == 0) return;
        ArrayList<E> newArrayList = new ArrayList<>(arrayList.size());
        newArrayList.addAll(arrayList);
        arrayList.clear();
        this.arrayCallback.process(newArrayList);

    }


}


