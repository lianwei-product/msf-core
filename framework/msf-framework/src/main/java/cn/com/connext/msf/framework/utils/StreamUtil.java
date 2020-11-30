package cn.com.connext.msf.framework.utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @Author: ming.wang
 * @Date: 2020/9/11 15:51
 * @Description:
 */
public class StreamUtil {
    //根据key去重
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> method) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(method.apply(t));
    }

    //消费型接口
    public static void consumer(Object accept, Consumer<Object> method) {
        method.accept(accept);
    }

    //消费型接口
    public static void consumer(long times, Object accept, Consumer<Object> method) {
        for (int i = 0; i < times; i++) {
            method.accept(accept);
        }
    }

}
