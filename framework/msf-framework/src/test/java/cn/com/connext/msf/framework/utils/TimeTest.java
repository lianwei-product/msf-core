package cn.com.connext.msf.framework.utils;

import cn.com.connext.msf.framework.entity.TagConditionExpModel;
import cn.com.connext.msf.framework.query.QueryOperators;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimeTest {
    @Test
    public void getLocalTime() throws Exception {
        System.out.println(Time.getLocalDate(1382694957));

        LocalDateTime eventTime = LocalDateTime.now();
        System.out.println(Long.parseLong(eventTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))));
    }

    @Test
    public void getLocalTimeFromString() {
        System.out.println(Time.parseDate("2011-12-03 10:15:30"));
        System.out.println(Time.parseDate("2011-12-03"));
        System.out.println(Time.parseDate("2011-12-03T10:15:30+01:00[Europe/Paris]"));
        System.out.println(Time.parseDate("2011-12-03T10:15:30+01:00"));
        System.out.println(Time.parseDate("2011-12-03T10:15:30"));

        LocalDateTime localDateTime = Time.parseLocalDateTime("2011-12-03 10:15:30", "Etc/GMT");

        System.out.println(localDateTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Test
    public void getTimeFormatString() throws Exception {
        LocalDateTime eventTime = LocalDateTime.now();
        System.out.println(Time.getTimeFormatString(eventTime));
    }

    @Test
    public void getRelativeTime() {
        LocalDateTime localDateTime = Time.getRelativeTime("-30d", QueryOperators.AF);
        System.out.println(localDateTime);
        System.out.println(Time.getRelativeTimeString("-30d", QueryOperators.AF));

        System.out.println();
        localDateTime = Time.getRelativeTime("-1M", QueryOperators.AF);
        System.out.println(localDateTime);
        localDateTime = Time.getRelativeTime("-1M", QueryOperators.BF);
        System.out.println(localDateTime);

        System.out.println();
        System.out.println("Last Month: ");
        localDateTime = Time.getRelativeTime("!-1M", QueryOperators.AF);
        System.out.println(">= " + localDateTime);
        localDateTime = Time.getRelativeTime("!-1M", QueryOperators.BF);
        System.out.println("< " + localDateTime);

        System.out.println();
        System.out.println("Next Month: ");
        localDateTime = Time.getRelativeTime("!1M", QueryOperators.AF);
        System.out.println(">= " + localDateTime);
        localDateTime = Time.getRelativeTime("!1M", QueryOperators.BF);
        System.out.println("< " + localDateTime);

        System.out.println();
        System.out.println("This Month1: ");
        localDateTime = Time.getRelativeTime("!0M", QueryOperators.AF);
        System.out.println(">= " + localDateTime);
        localDateTime = Time.getRelativeTime("!0M", QueryOperators.BF);
        System.out.println("< " + localDateTime);

        System.out.println();
        System.out.println("This Month2: ");
        localDateTime = Time.getRelativeTime("!-0M", QueryOperators.AF);
        System.out.println(">= " + localDateTime);
        localDateTime = Time.getRelativeTime("!+0M", QueryOperators.BF);
        System.out.println("< " + localDateTime);

        System.out.println();
        System.out.println("Last Year: ");
        localDateTime = Time.getRelativeTime("!-1y", QueryOperators.AF);
        System.out.println(">= " + localDateTime);
        localDateTime = Time.getRelativeTime("!-1y", QueryOperators.BF);
        System.out.println("< " + localDateTime);


        System.out.println();
        System.out.println("Next Year: ");
        localDateTime = Time.getRelativeTime("!1y", QueryOperators.AF);
        System.out.println(">= " + localDateTime);
        localDateTime = Time.getRelativeTime("!1y", QueryOperators.BF);
        System.out.println("< " + localDateTime);


        System.out.println();
        System.out.println("This Year: ");
        localDateTime = Time.getRelativeTime("!0y", QueryOperators.AF);
        System.out.println(">= " + localDateTime);
        localDateTime = Time.getRelativeTime("!0y", QueryOperators.BF);
        System.out.println("< " + localDateTime);

        System.out.println();
        System.out.println("3月-6月整月: ");
        localDateTime = Time.getRelativeTime("!-2M", QueryOperators.AF);
        System.out.println("> " + localDateTime);
        localDateTime = Time.getRelativeTime("!1M", QueryOperators.BF);
        System.out.println("< " + localDateTime);

        System.out.println();
        System.out.println("过去7天（整日）: ");
        localDateTime = Time.getRelativeTime("!-10M", QueryOperators.AF);
        System.out.println("> " + localDateTime);
        localDateTime = Time.getRelativeTime("!-10M", QueryOperators.BF);
        System.out.println("< " + localDateTime);
    }

    @Test
    public void testTimeRange() {
        System.out.println(Time.getStartTimeByUnit(LocalDateTime.now(), "y"));
        System.out.println(Time.getStartTimeByUnit(LocalDateTime.now(), "M"));
        System.out.println(Time.getStartTimeByUnit(LocalDateTime.now(), "d"));

        System.out.println();
        System.out.println(Time.getEndTimeByUnit(LocalDateTime.now(), "y"));
        System.out.println(Time.getEndTimeByUnit(LocalDateTime.now(), "M"));
        System.out.println(Time.getEndTimeByUnit(LocalDateTime.now(), "d"));

        System.out.println();
        LocalDateTime dateTime = LocalDateTime.of(2018, 2, 15, 12, 30, 45);
        System.out.println(Time.getStartTimeByUnit(dateTime, "y"));
        System.out.println(Time.getStartTimeByUnit(dateTime, "M"));
        System.out.println(Time.getStartTimeByUnit(dateTime, "d"));

        System.out.println();
        System.out.println(Time.getEndTimeByUnit(dateTime, "y"));
        System.out.println(Time.getEndTimeByUnit(dateTime, "M"));
        System.out.println(Time.getEndTimeByUnit(dateTime, "d"));
    }

    @Test
    public void testParseLocalDateTime() {
        String fieldValue = "2019-04-23T17:50:41+0800";
        LocalDateTime localDateTime = Time.parseLocalDateTime(fieldValue, TimeZone.getDefault().getID());
        fieldValue = localDateTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        System.out.println(fieldValue);
    }

    @Test
    public void getRelativeTime1() {
        LocalDateTime localDateTime = Time.getRelativeTime("-1d", QueryOperators.AF);
        System.out.println(localDateTime);
        localDateTime = Time.getRelativeTime("-1d", QueryOperators.BF);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime("!-1d", QueryOperators.AF);
        System.out.println(localDateTime);
        localDateTime = Time.getRelativeTime("!-1d", QueryOperators.BF);
        System.out.println(localDateTime);

        System.out.println();
        localDateTime = Time.getRelativeTime("-1M", QueryOperators.AF);
        System.out.println(localDateTime);
        localDateTime = Time.getRelativeTime("-1M", QueryOperators.BF);
        System.out.println(localDateTime);

        System.out.println();
        localDateTime = Time.getRelativeTime("!-1M", QueryOperators.AF);
        System.out.println(localDateTime);
        localDateTime = Time.getRelativeTime("!-1M", QueryOperators.BF);
        System.out.println(localDateTime);

        System.out.println();
        localDateTime = Time.getRelativeTime("-1y", QueryOperators.AF);
        System.out.println(localDateTime);
        localDateTime = Time.getRelativeTime("-1y", QueryOperators.BF);
        System.out.println(localDateTime);

        System.out.println();
        localDateTime = Time.getRelativeTime("!-1y", QueryOperators.AF);
        System.out.println(localDateTime);
        localDateTime = Time.getRelativeTime("!-1y", QueryOperators.BF);
        System.out.println(localDateTime);

    }

    @Test
    public void getRelativeTimeForRealTimeTag1() {
        LocalDateTime localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-10 09:00:00", "-1y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 11, 10, 9, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-15 09:00:00", "-1y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 11, 15, 9, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-06-15 09:00:00", "-6M", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 12, 15, 9, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-08-18 09:00:00", "-6M", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 2, 18, 9, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-05 18:00:00", "-2d", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 11, 07, 18, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-04 13:00:00", "-4d", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 11, 8, 13, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-10 09:00:00", "-2d", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 12, 12, 9, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-10 09:00:00", "-4d", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 12, 14, 9, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-02 09:00:00", "-1y", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 12, 2, 9, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2019-12-09 10:00:00", "1M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 11, 9, 10, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-03-08 10:00:00", "3M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 12, 8, 10, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-03-08 10:00:00", "1M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 8, 10, 0));
        System.out.println(localDateTime);
    }

    @Test
    public void getRelativeTimeForRealTimeTag2() {
        LocalDateTime localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-10 09:00:00", "!-1y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 1, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2019-01-10 09:00:00", "!-1y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2021, 1, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-02-05 09:00:00", "!-1y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2022, 1, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-10 09:00:00", "!-3y", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2021, 1, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2019-02-10 09:00:00", "!-3y", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2022, 1, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2019-10-10 13:00:00", "!-2M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 1, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-02-10 09:00:00", "!-2M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 5, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2019-08-08 10:00:00", "!-6M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 3, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-01-03 09:30:00", "!-1M", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-01-03 09:30:00", "!-6M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 8, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2019-11-10 09:00:00", "!-4d", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 11, 15, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-03-15 09:00:00", "!-4d", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 3, 20, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("1990-02-14 10:00:00", "!-35y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2026, 1, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-05-11 00:00:00", "!3M", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 1, 0, 0));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2020-05-11 00:00:00", "!1M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 5, 1, 0, 0));
        System.out.println(localDateTime);
    }

    @Test
    public void getRelativeTimeForRealTimeTag3() {
        LocalDateTime localDateTime = Time.getRealTimeTagLocalEventTime("2017-01-30 09:20:00", "-3y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 1, 30, 9, 20));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2017-01-30 09:20:00", "-3y", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 1, 30, 9, 20));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2017-01-30 09:20:00", "0y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2017, 1, 30, 9, 20));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2017-01-30 09:20:00", "0y", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2017, 1, 30, 9, 20));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2017-01-30 09:20:00", "5y", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2012, 1, 30, 9, 20));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2017-01-30 09:20:00", "5y", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2012, 1, 30, 9, 20));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-15 12:15:00", "-4M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 04, 15, 12, 15));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-15 12:15:00", "-4M", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 04, 15, 12, 15));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-15 12:15:00", "0M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 12, 15, 12, 15));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-15 12:15:00", "0M", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 12, 15, 12, 15));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-15 12:15:00", "5M", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 7, 15, 12, 15));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-12-15 12:15:00", "5M", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 7, 15, 12, 15));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-05 13:30:00", "-10d", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 11, 15, 13, 30));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-05 13:30:00", "-10d", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 11, 15, 13, 30));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-05 13:30:00", "0d", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 11, 5, 13, 30));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2018-11-05 13:30:00", "0d", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 11, 5, 13, 30));
        System.out.println(localDateTime);


        localDateTime = Time.getRealTimeTagLocalEventTime("2019-12-15 12:15:00", "20d", QueryOperators.AF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 11, 25, 12, 15));
        System.out.println(localDateTime);

        localDateTime = Time.getRealTimeTagLocalEventTime("2019-12-15 12:15:00", "20d", QueryOperators.BF);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 11, 25, 12, 15));
    }

    @Test
    public void getRelativeTimeForRealTimeTag4() {
        //AF >, BF <
        String exp = "!-3y";
        String dataTimeStr = "2017-01-30 09:20:00";
        String queryOperator = QueryOperators.AF;

        LocalDateTime localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        LocalDateTime expectTime = LocalDateTime.of(2021, 1, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(expectTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 1, 1, 0, 0));
        System.out.println(localDateTime);

        queryOperator = QueryOperators.BF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(expectTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2018, 1, 1, 0, 0));
        System.out.println(localDateTime);

        exp = "!0y";
        dataTimeStr = "2018-05-15 19:30:30";
        queryOperator = QueryOperators.AF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2019, 1, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(expectTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 1, 1, 0, 0));
        System.out.println(localDateTime);

        queryOperator = QueryOperators.BF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2018, 1, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(localDateTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2019, 1, 1, 0, 0));
        System.out.println(localDateTime);

        exp = "!2y";
        dataTimeStr = "2020-01-10 15:16:30";
        queryOperator = QueryOperators.AF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2019, 1, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(expectTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2021, 1, 1, 0, 0));
        System.out.println(localDateTime);

        queryOperator = QueryOperators.BF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2018, 1, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(localDateTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2021, 1, 1, 0, 0));
        System.out.println(localDateTime);


        exp = "!-6M";
        dataTimeStr = "2020-01-10 15:16:30";
        queryOperator = QueryOperators.AF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2020, 8, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(expectTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 1, 0, 0));
        System.out.println(localDateTime);

        queryOperator = QueryOperators.BF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2020, 7, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(localDateTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 1, 0, 0));
        System.out.println(localDateTime);

        exp = "!0M";
        dataTimeStr = "2020-01-10 15:16:30";
        queryOperator = QueryOperators.AF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2020, 2, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(expectTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 1, 0, 0));
        System.out.println(localDateTime);

        queryOperator = QueryOperators.BF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(localDateTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 1, 0, 0));
        System.out.println(localDateTime);


        exp = "!3M";
        dataTimeStr = "2020-01-10 15:16:30";
        queryOperator = QueryOperators.AF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2019, 11, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(expectTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 1, 0, 0));
        System.out.println(localDateTime);

        queryOperator = QueryOperators.BF;

        localDateTime = Time.getRealTimeTagLocalEventTime(dataTimeStr, exp, queryOperator);
        expectTime = LocalDateTime.of(2019, 10, 1, 0, 0);
        Assert.assertEquals(localDateTime, expectTime);
        System.out.println(localDateTime);

        localDateTime = Time.getRelativeTime(localDateTime, exp, queryOperator, false);
        Assert.assertEquals(localDateTime, LocalDateTime.of(2020, 2, 1, 0, 0));
        System.out.println(localDateTime);
    }

    @Test
    public void getLocalTimeFromString1() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 4, 1, 0, 0);
        System.out.println(Time.getRelativeTime(localDateTime, "!1M", QueryOperators.BF, false));

        localDateTime = LocalDateTime.of(2020, 5, 1, 0, 0);
        System.out.println(Time.getRelativeTime(localDateTime, "!1M", QueryOperators.AF, false));
    }

    @Test
    public void testTagConditionExpModel() {
        String exp = "!-1M";
        TagConditionExpModel model = TagConditionExpModel.from(exp);
        System.out.println(JSON.toJsonString(model));
    }

    @Test
    public void testParseLocalDateTime1() {
        String dateTimeZoneId = "Asia/Shanghai";
        LocalDateTime localDateTime = Time.parseLocalDateTime("2019-12-21T00:00:01.000+0800", dateTimeZoneId);

        System.out.println(localDateTime);

        dateTimeZoneId = "GMT";
        localDateTime = Time.parseLocalDateTime("2019-12-21T00:00:01.000+0800", dateTimeZoneId);

        System.out.println(localDateTime);

        dateTimeZoneId = "Asia/Shanghai";
        localDateTime = Time.parseLocalDateTime("2019-12-20T16:00:01Z", dateTimeZoneId);

        System.out.println(localDateTime);

        dateTimeZoneId = "Asia/Shanghai";
        localDateTime = Time.parseLocalDateTime("2019-12-21T00:00:01Z", dateTimeZoneId);

        System.out.println(localDateTime);

        dateTimeZoneId = "Asia/Shanghai";
        localDateTime = Time.parseLocalDateTime("2019-12-21T00:00:01.000+0800", dateTimeZoneId);

        String dataValue = Time.getTimeFormatString(localDateTime, dateTimeZoneId, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(dataValue);

        dataValue = Time.getTimeFormatString(localDateTime, dateTimeZoneId, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println(dataValue);

        dataValue = localDateTime.atZone(ZoneId.of("GMT")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println(dataValue);
    }

    @Test
    public void getTimeFormatString1() throws Exception {
        Date currentTime = new Date();
        System.out.println(Time.getTimeFormatString(currentTime,"yyyy.MM.dd"));
        System.out.println(Time.getTimeFormatString(currentTime,"yyyy.MM"));
    }
}