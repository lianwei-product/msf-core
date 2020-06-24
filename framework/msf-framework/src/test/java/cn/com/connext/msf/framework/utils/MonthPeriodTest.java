package cn.com.connext.msf.framework.utils;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class MonthPeriodTest {

    @Test
    public void test() {
        LocalDateTime exceptTime = LocalDateTime.now().plusMonths(-1);
        LocalDateTime actualTime;

        actualTime = MonthPeriod.getFrom("-1F"); // 上月上旬起始日期
        System.out.println(actualTime);
        Assert.assertEquals(exceptTime.getYear(), actualTime.getYear());
        Assert.assertEquals(exceptTime.getMonth(), actualTime.getMonth());
        Assert.assertEquals(1, actualTime.getDayOfMonth());

        actualTime = MonthPeriod.getTo("-1F"); // 上月上旬结束日期
        System.out.println(actualTime);
        Assert.assertEquals(exceptTime.getYear(), actualTime.getYear());
        Assert.assertEquals(exceptTime.getMonth(), actualTime.getMonth());
        Assert.assertEquals(16, actualTime.getDayOfMonth());

        actualTime = MonthPeriod.getFrom("-1L"); // 上月下旬起始日期
        System.out.println(actualTime); // 2019-04-16T00:00
        Assert.assertEquals(exceptTime.getYear(), actualTime.getYear());
        Assert.assertEquals(exceptTime.getMonth(), actualTime.getMonth());
        Assert.assertEquals(16, actualTime.getDayOfMonth());

        actualTime = MonthPeriod.getTo("-1L"); // 上月下旬结束日期
        System.out.println(actualTime); // 2019-04-16T00:00
        Assert.assertEquals(exceptTime.getMonth().plus(1), actualTime.getMonth());
        Assert.assertEquals(1, actualTime.getDayOfMonth());
    }


    @Test
    public void test02() {
        String actualTime;

        actualTime = MonthPeriod.getFromString("-1F"); // 上月上旬起始日期
        System.out.println(actualTime);

        actualTime = MonthPeriod.getToString("-1F");  // 上月上旬结束日期
        System.out.println(actualTime);

        actualTime = MonthPeriod.getFromString("-1L"); // 上月下旬起始日期
        System.out.println(actualTime); // 2019-04-16T00:00

        actualTime = MonthPeriod.getToString("-1L"); // 上月下旬结束日期
        System.out.println(actualTime); // 2019-04-16T00:00
    }

    @Test
    public void test03() {
        LocalDateTime realTimeTagLocalEventTime;

        LocalDateTime localDateTime = LocalDateTime.of(2019, 11, 10, 0, 0);
        LocalDateTime searchBeginTime = LocalDateTime.of(2019, 10, 1, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime, "-1F");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2019,12,1,0,0));

        localDateTime = LocalDateTime.of(2019, 11, 10, 0, 0);
        searchBeginTime = LocalDateTime.of(2019, 11, 1, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime, "-1F");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2020,1,1,0,0));

        localDateTime = LocalDateTime.of(2019, 11, 16, 0, 0);
        searchBeginTime = LocalDateTime.of(2019, 9, 1, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime, "-3F");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2020,2,1,0,0));

        localDateTime = LocalDateTime.of(2019, 11, 16, 0, 0);
        searchBeginTime = LocalDateTime.of(2019, 11, 1, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime, "-3F");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2020,3,1,0,0));

        localDateTime = LocalDateTime.of(2019, 12, 1, 0, 0);
        searchBeginTime = LocalDateTime.of(2019, 11, 16, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime, "-1L");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2020,1,1,0,0));

        localDateTime = LocalDateTime.of(2019, 12, 1, 0, 0);
        searchBeginTime = LocalDateTime.of(2019, 12, 16, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime, "-1L");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2020,2,1,0,0));

        localDateTime = LocalDateTime.of(2020, 3, 2, 0, 0);
        searchBeginTime = LocalDateTime.of(2019, 12, 16, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime,"-1L");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2020,2,1,0,0));

        localDateTime = LocalDateTime.of(2020, 3, 2, 0, 0);
        searchBeginTime = LocalDateTime.of(2020, 3, 1, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime,"-1L");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2020,5,1,0,0));

        localDateTime = LocalDateTime.of(2020, 3, 2, 0, 0);
        searchBeginTime = LocalDateTime.of(2019, 12, 16, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime,"3L");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2019,12,1,0,0));

        localDateTime = LocalDateTime.of(2020, 3, 2, 0, 0);
        searchBeginTime = LocalDateTime.of(2020, 3, 1, 0, 0);
        realTimeTagLocalEventTime = MonthPeriod.getRealTimeTagLocalEventTime(localDateTime, searchBeginTime,"3L");
        System.out.println(realTimeTagLocalEventTime);
        Assert.assertEquals(realTimeTagLocalEventTime, LocalDateTime.of(2020,1,1,0,0));


    }
}