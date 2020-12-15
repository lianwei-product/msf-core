package cn.com.connext.msf.framework.distributeid.domain;

import cn.com.connext.msf.framework.distributeid.provider.ClusterNodeNameProvider;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分布式标识生成器
 */
public class DistributeIdGenerator {

    private final Object lock = new Object();
    private final Random random = new Random();
    private final String padStr = "0";
    private final Map<String, Counter> counters = Maps.newHashMap();

    private final int serialNumberLength;
    private final int randomNumberLength;
    private final int maxRandomNumber;
    private final DateTimeFormatter dateTimeFormatter;
    private final TimeUnit refreshSerialNumberTimeUnit;

    private final ClusterNodeNameProvider clusterNodeNameProvider;

    public DistributeIdGenerator(@Value("${msf.distribute-id.serial-number-length:8}") int serialNumberLength,
                                 @Value("${msf.distribute-id.random-number-length:2}") int randomNumberLength,
                                 @Value("${msf.distribute-id.time-format-partner:yyMMddHHmmss}") String timeFormatPattern,
                                 ClusterNodeNameProvider clusterNodeNameProvider) {
        this.serialNumberLength = serialNumberLength;
        this.randomNumberLength = randomNumberLength;
        this.maxRandomNumber = Integer.parseInt(StringUtils.rightPad("1", randomNumberLength + 1, padStr));
        this.refreshSerialNumberTimeUnit = getRefreshTimeUnit(timeFormatPattern);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(timeFormatPattern);
        this.clusterNodeNameProvider = clusterNodeNameProvider;
    }

    /**
     * 创建并返回一个新的Id，{prefix}{time}{serialNumber}{randomNumber}{node}
     *
     * @param type   标识类型，不同类型的标识会采用不同的计数器，如：audi_order，bmw_member
     * @param prefix 标识前缀，如采用品牌名称
     */
    public String newId(String type, String prefix) {
        DistributeIdInfo info = getDistributeIdInfo(type);
        return prefix != null
                ? prefix.concat(info.time).concat(info.serialNumber).concat(info.randomNumber).concat(info.node)
                : info.time.concat(info.serialNumber).concat(info.randomNumber).concat(info.node);
    }

    /**
     * 创建并返回一个新的带有分隔符的Id,{prefix}_{time}_{serialNumber}_{randomNumber}_{node}
     *
     * @param type   标识类型，不同类型的标识会采用不同的计数器，如：audi_order，bmw_member
     * @param prefix 标识前缀，如采用品牌名称
     */
    public String newSeparateId(String type, String prefix) {
        DistributeIdInfo info = getDistributeIdInfo(type);
        return randomNumberLength == 0
                ? prefix.concat(info.time).concat("_").concat(info.serialNumber).concat("_").concat(info.node)
                : prefix.concat(info.time).concat("_").concat(info.serialNumber).concat("_").concat(info.randomNumber).concat("_").concat(info.node);
    }

    private DistributeIdInfo getDistributeIdInfo(String type) {
        Instant instant = Instant.now();
        Counter counter = counters.get(type);
        if (counter == null || counter.expired(instant)) {
            synchronized (lock) {
                counter = counters.get(type);
                if (counter == null || counter.expired(instant)) {
                    counters.put(type, new Counter(instant));
                    counter = counters.get(type);
                }
            }
        }
        return new DistributeIdInfo(counter.time, counter.atomicInteger.addAndGet(1));
    }


    private TimeUnit getRefreshTimeUnit(String timeFormatPattern) {
        if (timeFormatPattern.endsWith("s")) {
            return TimeUnit.SECONDS;
        }

        if (timeFormatPattern.endsWith("m")) {
            return TimeUnit.MINUTES;
        }

        if (timeFormatPattern.endsWith("H")) {
            return TimeUnit.HOURS;
        }

        if (timeFormatPattern.endsWith("d")) {
            return TimeUnit.DAYS;
        }

        throw new RuntimeException(MessageFormat.format("TimeFormatPattern not valid: {0}", timeFormatPattern));
    }


    class DistributeIdInfo {
        String time;
        String node;
        String serialNumber;
        String randomNumber;

        DistributeIdInfo(String time, int number) {
            this.time = time;
            this.node = clusterNodeNameProvider.getClusterNodeName();
            this.serialNumber = StringUtils.leftPad(Integer.toString(number), serialNumberLength, padStr);
            this.randomNumber = randomNumberLength == 0
                    ? ""
                    : StringUtils.leftPad(Integer.toString(random.nextInt(maxRandomNumber)), randomNumberLength, padStr);
        }
    }


    class Counter {
        String time;
        long expiresTime;
        AtomicInteger atomicInteger;

        Counter(Instant instant) {
            this.time = dateTimeFormatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
            this.expiresTime = getExpiresTime(instant);
            this.atomicInteger = new AtomicInteger(0);
        }

        boolean expired(Instant instant) {
            return this.expiresTime < getExpiresTime(instant);
        }

        private long getExpiresTime(Instant instant) {
            long currentTime = instant.toEpochMilli();
            switch (refreshSerialNumberTimeUnit) {
                case SECONDS:
                    return currentTime / 1000;

                case MINUTES:
                    return currentTime / 60000;

                case HOURS:
                    return currentTime / 3600000;

                default:
                    return LocalDateTime.of(
                            LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate(),
                            LocalTime.MIN
                    ).toEpochSecond(ZoneOffset.UTC);
            }
        }
    }

}
