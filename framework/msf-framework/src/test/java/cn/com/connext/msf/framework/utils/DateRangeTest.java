package cn.com.connext.msf.framework.utils;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.TimeZone;

public class DateRangeTest {

    @Test
    public void test() {
        LocalDateTime actualTime = LocalDateTime.now(); //本日

        boolean isValid = DateRange.validDataRange(actualTime, "0M,0M");    //本月生日
        Assert.assertEquals(isValid, true);

        isValid = DateRange.validDataRange(actualTime, "0d,0d");             //本日生日
        Assert.assertEquals(isValid, true);

        actualTime = LocalDateTime.now().plusMonths(-1);//相对时间-上个月

        isValid = DateRange.validDataRange(actualTime, "-1M,-1M");   //上个月生日
        Assert.assertEquals(isValid, true);

        isValid = DateRange.validDataRange(actualTime, "1M,1M");             //下个月生日
        Assert.assertEquals(isValid, false);

        actualTime = LocalDateTime.now().plusMonths(2); //相对时间-下两个月

        isValid = DateRange.validDataRange(actualTime, "1M,1M");             //下个月生日
        Assert.assertEquals(isValid, false);


        isValid = DateRange.validDataRange(actualTime, "2M,2M");             //下两个月内生日
        Assert.assertEquals(isValid, true);

        actualTime = LocalDateTime.now().plusDays(1);   //相对时间-明天

        isValid = DateRange.validDataRange(actualTime, "1d,1d");             //明天生日
        Assert.assertEquals(isValid, true);


        actualTime = LocalDateTime.of(2016, 2, 29, 0, 0);
        isValid = DateRange.validDataRange(actualTime, "-3M,3M");
        Assert.assertEquals(isValid, true);

        actualTime = LocalDateTime.of(2015, 4, 26, 0, 0);
        isValid = DateRange.validDataRange(actualTime, "-1M,0M");
        Assert.assertEquals(isValid, false);
    }

    @Test
    public void test2() {
        LocalDateTime relativeTime = DateRange.getRealTimeTagLocalEventTime("1986-01-05", "1M,1M", true);
        System.out.println(relativeTime);

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1986-02-05", "1M,1M", false);
        System.out.println(relativeTime);

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1986-06-08", "1d,1d", false);
        System.out.println(relativeTime);

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1986-02-07", "3d,3d", true);
        System.out.println(relativeTime);

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1986-02-07", "3d,3d", true);
        System.out.println(relativeTime);

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1986-07-05", "-3M,3M", false);
        System.out.println(relativeTime);

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1986-03-05", "-3M,3M", true);
        System.out.println(relativeTime);
    }

    @Test
    public void test3() {
        String dayExp = "3d,3d";
        String actualValue = "1999-08-24T00:00:00+08:00";
        LocalDateTime dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, dayExp),false);

        actualValue = "1999-01-02T00:00:00+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, dayExp),false);

        actualValue = "1999-01-01T00:00:00+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, dayExp),true);

        actualValue = "1999-01-01T23:59:59+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, dayExp),true);

        actualValue = "2019-01-01T23:59:59+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, dayExp),true);

        actualValue = "2020-01-01T23:59:59+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, dayExp),true);
    }

    @Test
    public void test4() {
        String monthExp = "1M,1M";

        String actualValue = "1999-08-24T00:00:00+08:00";
        LocalDateTime dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, monthExp),false);

        actualValue = "2020-12-31T23:59:59+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, monthExp),false);

        actualValue = "1999-01-02T00:00:00+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, monthExp),true);

        actualValue = "1999-01-01T00:00:00+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, monthExp),true);

        actualValue = "2020-01-31T23:59:59+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, monthExp),true);

        actualValue = "2019-01-15T23:59:59+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, monthExp),true);

        actualValue = "2020-01-20T23:59:59+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, monthExp),true);

        actualValue = "1990-01-01T23:59:59+08:00";
        dateTime = Time.parseLocalDateTime(actualValue, TimeZone.getDefault().getID());
        Assert.assertEquals(DateRange.validDataRange(dateTime, monthExp),true);
    }


    @Test
    public void test5() {
        LocalDateTime relativeTime = DateRange.getRealTimeTagLocalEventTime("1986-01-05", "0M,0M", true);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,2,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2015-03-15", "0M,0M", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,3,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2015-12-15", "0M,0M", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,12,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1988-11-15", "-2M,-2M", true);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,2,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1988-11-15", "-2M,-2M", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2021,1,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("1988-01-15", "-2M,-2M", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,3,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-04-15", "3M,3M", true);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,2,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-12-15", "3M,3M", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,9,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-02-15", "3M,3M", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,11,1,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-01-10", "0d,0d", true);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,1,11,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-02-20", "0d,0d", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,2,20,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-01-10", "0d,0d", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2021,1,10,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-01-07", "-3d,-3d", true);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,1,11,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-01-05", "-3d,-3d", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2021,1,8,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-10-07", "-3d,-3d", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,10,10,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-01-13", "3d,3d", true);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,1,11,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-02-15", "3d,3d", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2020,2,12,0,0));

        relativeTime = DateRange.getRealTimeTagLocalEventTime("2000-01-05", "3d,3d", false);
        System.out.println(relativeTime);
        Assert.assertEquals(relativeTime, LocalDateTime.of(2021,1,2,0,0));
    }

}