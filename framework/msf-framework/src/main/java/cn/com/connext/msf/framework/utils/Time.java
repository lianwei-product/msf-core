package cn.com.connext.msf.framework.utils;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.query.QueryOperators;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Chenghan on 2017/12/12.
 */
public class Time {
    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static List<DateTimeFormatter> LOCAL_DATE_TIME_FORMATTERS;
    private final static List<DateTimeFormatter> ZONED_DATE_TIME_FORMATTERS;
    public final static List<String> RELATIVE_TIME_UNITS;

    static {
        LOCAL_DATE_TIME_FORMATTERS = Lists.newArrayList();
        LOCAL_DATE_TIME_FORMATTERS.add(DATE_TIME_FORMATTER);                     // 2011-12-03 10:15:30
        LOCAL_DATE_TIME_FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);   // 2011-12-03T10:15:30

        ZONED_DATE_TIME_FORMATTERS = Lists.newArrayList();
        ZONED_DATE_TIME_FORMATTERS.add(DateTimeFormatter.ISO_DATE_TIME);         // 2011-12-03T10:15:30+01:00[Europe/Paris]
        ZONED_DATE_TIME_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));  // 2011-12-03T10:15:30+01:00
        ZONED_DATE_TIME_FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));  // 2011-12-03T10:15:30+0100

        RELATIVE_TIME_UNITS = new ArrayList<>(Arrays.asList("y", "M", "d", "h", "m", "s", "年", "月", "日", "时", "分", "秒"));
    }

    public static Date getUtcBeginTime() {
        return new Date(-28800000);
    }

    public static Date getLocalDate(int timestamp) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.add(Calendar.MILLISECOND, TimeZone.getDefault().getRawOffset());
        calendar.add(Calendar.SECOND, timestamp);
        return calendar.getTime();
    }

    public static Date getCurrentDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime day = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0);
        return Date.from(day.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getTimeFormatString(Date currentTime) {
        return getTimeFormatString(currentTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTimeFormatString(LocalDateTime currentTime) {
        return currentTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String getTimeFormatString(LocalDateTime currentTime, String dateTimeZoneId, DateTimeFormatter dateTimeFormatter) {
        return currentTime.atZone(ZoneId.of(dateTimeZoneId)).format(dateTimeFormatter);
    }


    public static String getTimeFormatString(Date currentTime, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    public static Date parseDate(String string) {
        Date dateTime;
        try {
            dateTime = Date.from(parseLocalDateTime(string, TimeZone.getDefault().getID()).atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception ex) {
            throw new BusinessException("Invalid date: " + string);
        }
        return dateTime;
    }

    public static LocalDateTime parseLocalDateTime(String string, String defaultTimezoneId) {
        if (string != null && string.length() >= 10) {
            switch (string.length()) {
                case 10:
                    try {
                        LocalDateTime localDateTime = parseBeginTime(string);
                        if (localDateTime != null) {
                            return localDateTime;
                        }
                    } catch (Exception e) {
                        // do noting.
                    }
                    break;

                case 19:
                    for (DateTimeFormatter dateTimeFormatter : LOCAL_DATE_TIME_FORMATTERS) {
                        try {
                            if (TimeZone.getDefault().getID().equals(defaultTimezoneId)) {
                                return LocalDateTime.parse(string, dateTimeFormatter);
                            } else {
                                return LocalDateTime.parse(string, dateTimeFormatter)
                                        .atZone(ZoneId.of(defaultTimezoneId))
                                        .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                            }
                        } catch (Exception e) {
                            // do noting.
                        }
                    }
                    break;

                default:
                    for (DateTimeFormatter dateTimeFormatter : ZONED_DATE_TIME_FORMATTERS) {
                        try {
                            return ZonedDateTime.parse(string, dateTimeFormatter).withZoneSameInstant(ZoneId.of(defaultTimezoneId)).toLocalDateTime();
                        } catch (Exception e) {
                            // do noting.
                        }
                    }
                    break;
            }
        }

        throw new RuntimeException("error dateTime format: " + string);
    }

    public static LocalDateTime parseBeginTime(String string) {
        if (StringUtils.isEmpty(string)) return null;
        try {
            if (!string.contains(":")) string = string + " 00:00:00";
            return LocalDateTime.parse(string, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime parseEndTime(String string) {
        if (StringUtils.isEmpty(string)) return null;
        try {
            if (!string.contains(":")) {
                string = string + " 00:00:00";
                return LocalDateTime.parse(string, DATE_TIME_FORMATTER).plusDays(1);
            } else {
                return LocalDateTime.parse(string, DATE_TIME_FORMATTER);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRelativeTimeString(String exp, String queryOperator) {
        return getTimeFormatString(getRelativeTime(exp, queryOperator));
    }

    public static LocalDateTime getRelativeTime(String exp, String queryOperator) {
        return getRelativeTime(LocalDateTime.now(), exp, queryOperator, false);
    }

    public static LocalDateTime getStartTimeByUnit(LocalDateTime dateTime, String unit) {
        return getStartTimeByUnit(dateTime, unit, false);
    }

    public static LocalDateTime getStartTimeByUnit(LocalDateTime dateTime, String unit, boolean isForRealTimeTag) {
        LocalDateTime startTime;
        switch (unit) {
            case "y":
            case "年":
                startTime = LocalDateTime.of(dateTime.getYear(), 1, 1, 0, 0, 0);
                if (isForRealTimeTag) {
                    startTime = startTime.plusYears(1);
                }
                break;
            case "M":
            case "月":
                startTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), 1, 0, 0, 0);
                if (isForRealTimeTag) {
                    startTime = startTime.plusMonths(1);
                }
                break;
            default:
                startTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 0, 0, 0);
                if (isForRealTimeTag) {
                    startTime = startTime.plusDays(1);
                }
                break;
        }
        return startTime;
    }

    public static LocalDateTime getEndTimeByUnit(LocalDateTime dateTime, String unit) {
        return getEndTimeByUnit(dateTime, unit, false);
    }

    public static LocalDateTime getEndTimeByUnit(LocalDateTime dateTime, String unit, boolean isForRealTimeTag) {
        LocalDateTime localDateTime = getStartTimeByUnit(dateTime, unit, isForRealTimeTag);
        switch (unit) {
            case "y":
            case "年":
                return localDateTime.plusYears(isForRealTimeTag ? -1 : 1);

            case "M":
            case "月":
                return localDateTime.plusMonths(isForRealTimeTag ? -1 : 1);

            default:
                return localDateTime.plusDays(isForRealTimeTag ? -1 : 1);
        }
    }

    private static LocalDateTime getLocalDateTime(LocalDateTime localDateTime, String unit, String exp, String queryOperator, boolean isForRealTimeTag) {
        if (exp.contains("!")) {
            int endIndex = exp.length();
            exp = exp.substring(1, endIndex);
            switch (queryOperator) {
                case QueryOperators.AF:
                    localDateTime = getStartTimeByUnit(localDateTime, unit, isForRealTimeTag);
                    break;
                case QueryOperators.BF:
                    localDateTime = getEndTimeByUnit(localDateTime, unit, isForRealTimeTag);
                    break;
            }
        }

        switch (unit) {
            case "y":
            case "年":
                localDateTime = localDateTime.plusYears(getTimeSpan(exp, isForRealTimeTag));
                break;

            case "M":
            case "月":
                localDateTime = localDateTime.plusMonths(getTimeSpan(exp, isForRealTimeTag));
                break;

            default:
                localDateTime = localDateTime.plusDays(getTimeSpan(exp, isForRealTimeTag));
                break;
        }

        return localDateTime;
    }

    public static Date getRealTimeTagEventTime(String dateTimeStr, String exp, String queryOperator) {
        return Date.from(getRealTimeTagLocalEventTime(dateTimeStr, exp, queryOperator).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 根据传入的时间，结合表达式和查询操作符，计算实时标签唤醒时间
     *
     * @param dateTimeStr
     * @param exp
     * @param queryOperator
     * @return
     */
    public static LocalDateTime getRealTimeTagLocalEventTime(String dateTimeStr, String exp, String queryOperator) {
        LocalDateTime localDateTime = parseLocalDateTime(dateTimeStr, TimeZone.getDefault().getID());
        return getRelativeTime(localDateTime, exp, queryOperator, true);
    }

    public static LocalDateTime getRelativeTime(LocalDateTime localDateTime, String exp, String queryOperator, boolean isForRealTimeTag) {
        String unit = "d";
        for (String tmpUnit : RELATIVE_TIME_UNITS) {
            if (exp.endsWith(tmpUnit)) {
                unit = tmpUnit;
                exp = exp.substring(0, exp.length() - 1);
                break;
            }
        }

        switch (unit) {
            case "y":
            case "年":
            case "M":
            case "月":
            case "d":
            case "日":
                return getLocalDateTime(localDateTime, unit, exp, queryOperator, isForRealTimeTag);

            case "h":
            case "时":
                return localDateTime.plusHours(getTimeSpan(exp, isForRealTimeTag));

            case "m":
            case "分":
                return localDateTime.plusMinutes(getTimeSpan(exp, isForRealTimeTag));

            default:
                return localDateTime.plusSeconds(getTimeSpan(exp, isForRealTimeTag));
        }
    }

    private static long getTimeSpan(String exp, boolean isForRealTimeTag) {
        long num = Long.parseLong(exp);
        if (isForRealTimeTag) {
            num = num * -1;
        }
        return num;
    }
}
