package cn.com.connext.msf.framework.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.TimeZone;

public class MonthPeriod {

    public static String getFromString(String exp) {
        return Time.getTimeFormatString(getFrom(exp));
    }

    public static String getToString(String exp) {
        return Time.getTimeFormatString(getTo(exp));
    }

    /**
     * 根据表达式，返回相对月的上、下旬起始日期
     *
     * @param exp 表达式，-1F(First Period) 表示上月上旬，-1L(Last Period)表示上月下旬
     * @return 起始日期
     */
    public static LocalDateTime getFrom(String exp) {
        validate(exp);
        String period = exp.substring(exp.length() - 1, exp.length());
        LocalDateTime begin = getMonthBeginTime(LocalDateTime.now(), exp);
        switch (period) {
            case "F":
                return begin;

            default:
                return LocalDateTime.of(begin.getYear(), begin.getMonth(), 16, 0, 0);
        }
    }


    public static LocalDateTime getTo(String exp) {
        validate(exp);
        String period = exp.substring(exp.length() - 1, exp.length());
        LocalDateTime begin = getMonthBeginTime(LocalDateTime.now(), exp);
        switch (period) {
            case "F":
                return LocalDateTime.of(begin.getYear(), begin.getMonth(), 15, 0, 0).plusDays(1);

            default:
                return begin.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);
        }
    }

    private static LocalDateTime getMonthBeginTime(LocalDateTime dateTime, String exp) {
        long deltaMonth = Long.parseLong(exp.substring(0, exp.length() - 1));
        return getMonthBeginTime(dateTime).plusMonths(deltaMonth);
    }

    private static LocalDateTime getMonthBeginTime(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), 1, 0, 0);
    }

    private static void validate(String exp) {
        if (!exp.endsWith("F") && !exp.endsWith("L")) {
            throw new RuntimeException("Invalid exp format, exp must end with char 'F' or 'D'.");
        }
    }

    public static Date getRealTimTagEventTime(String dateTimeStr, LocalDateTime searchBeginTime, String exp) {
        LocalDateTime dateTime = Time.parseLocalDateTime(dateTimeStr, TimeZone.getDefault().getID());
        return Date.from(getRealTimeTagLocalEventTime(dateTime, searchBeginTime, exp).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime getRealTimeTagLocalEventTime(LocalDateTime dateTime, LocalDateTime searchBeginTime, String exp) {
        LocalDateTime dateBeginTime = getMonthBeginTime(dateTime);
        LocalDateTime currentBeginTime = getMonthBeginTime(searchBeginTime);

        LocalDateTime localEventTime = dateBeginTime.equals(currentBeginTime) ? dateBeginTime.plusMonths(1) : dateBeginTime;

        String timeSpanStr = exp.substring(0, exp.length() - 1);
        long timeSpan = Long.parseLong(timeSpanStr) * -1;
        return localEventTime.plusMonths(timeSpan);
    }
}
