package cn.com.connext.msf.framework.utils;

import cn.com.connext.msf.framework.entity.DataRangeModel;
import cn.com.connext.msf.framework.query.QueryOperators;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.TimeZone;

public class DateRange {
    public static boolean validDataRange(LocalDateTime birthday, String exp) {
        DataRangeModel dataRangeModel = buildDateRangeModel(exp);
        LocalDateTime startTime = dataRangeModel.getStartTime();
        LocalDateTime endTime = dataRangeModel.getEndTime();

        return isDataRangeMatch(startTime, endTime, birthday);
    }

    private static boolean isDataRangeMatch(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime birthday) {
        LocalDateTime currentBirthday = getCurrentBirthday(startTime, birthday);
        boolean isMatch = isMatch(startTime, endTime, currentBirthday);

        if (startTime.getYear() != endTime.getYear()) {
            currentBirthday = getCurrentBirthday(endTime, birthday);
            isMatch = isMatch || isMatch(startTime, endTime, currentBirthday);
        }

        return isMatch;
    }

    private static boolean isMatch(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime currentBirthday) {
        long startTimeMillis = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()).getTime();
        long endTimeMillis = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()).getTime();
        long birthdayTimeMillis = Date.from(currentBirthday.atZone(ZoneId.systemDefault()).toInstant()).getTime();

        return birthdayTimeMillis >= startTimeMillis && birthdayTimeMillis < endTimeMillis;
    }

    private static void validate(String exp) {
        String[] expList = exp.split(",", -1);
        if (expList.length != 2) {
            throw new RuntimeException("Invalid exp format, exp:" + exp);
        }
        String startExp = expList[0].trim();
        String endExp = expList[1].trim();
        validSingleExp(startExp);
        validSingleExp(endExp);
    }

    private static void validSingleExp(String exp) {
        if (!exp.endsWith("M") && !exp.endsWith("d")) {
            throw new RuntimeException("Invalid exp format, exp must end with char 'M' or 'd'.");
        }
    }

    private static DataRangeModel buildDateRangeModel(String exp) {
        validate(exp);

        DataRangeModel dataRangeModel = new DataRangeModel();

        String[] expList = exp.split(",", -1);
        String startExp = expList[0].trim();
        String endExp = expList[1].trim();

        startExp = "!" + startExp;
        dataRangeModel.setStartExp(startExp);
        dataRangeModel.setStartTime(Time.getRelativeTime(startExp, QueryOperators.AF));

        endExp = "!" + endExp;
        dataRangeModel.setEndExp(endExp);
        dataRangeModel.setEndTime(Time.getRelativeTime(endExp, QueryOperators.BF));

        return dataRangeModel;
    }

    private static LocalDateTime getCurrentBirthday(LocalDateTime currentTime, LocalDateTime birthday) {
        LocalDateTime currentBeginMonth = LocalDateTime.of(currentTime.getYear(), birthday.getMonthValue(), 1, 0, 0);
        LocalDateTime currentEndMonth = currentBeginMonth.with(TemporalAdjusters.lastDayOfMonth());

        int dayOfMonth = birthday.getDayOfMonth() > currentEndMonth.getDayOfMonth() ? currentEndMonth.getDayOfMonth() : birthday.getDayOfMonth();
        return LocalDateTime.of(currentTime.getYear(), birthday.getMonthValue(), dayOfMonth, 0, 0);
    }

    public static Date getRealTimTagEventTime(String birthdayStr, String exp, boolean isMatch) {
        return Date.from(getRealTimeTagLocalEventTime(birthdayStr, exp, isMatch).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime getRealTimeTagLocalEventTime(String birthdayStr, String exp, boolean isMatch) {
        LocalDateTime birthday = Time.parseLocalDateTime(birthdayStr, TimeZone.getDefault().getID());
        return getRealTimeTagLocalEventTime(birthday, exp, isMatch);
    }

    public static LocalDateTime getRealTimeTagLocalEventTime(LocalDateTime birthday, String exp, boolean isMatch) {
        DataRangeModel dataRangeModel = buildDateRangeModel(exp);
        String startExp = dataRangeModel.getStartExp();
        String endExp = dataRangeModel.getEndExp();
        LocalDateTime startTime = dataRangeModel.getEndTime();
        LocalDateTime endTime = dataRangeModel.getEndTime();

        LocalDateTime relativeTime;

        if (isMatch) {
            relativeTime = getRelativeTime(startTime, birthday, startExp, QueryOperators.AF);
        } else {
            relativeTime = getRelativeTime(endTime, birthday, endExp, QueryOperators.BF);
        }

        if (Date.from(relativeTime.atZone(ZoneId.systemDefault()).toInstant()).getTime() < System.currentTimeMillis()) {
            relativeTime = relativeTime.plusYears(1);
        }

        return relativeTime;
    }

    private static LocalDateTime getRelativeTime(LocalDateTime currentTime, LocalDateTime birthday, String exp, String operator) {
        LocalDateTime relativeTime = null;
        if (currentTime != null) {
            LocalDateTime currentBirthday = getCurrentBirthday(currentTime, birthday);
            relativeTime = Time.getRelativeTime(currentBirthday, exp, operator, true);
        }
        return relativeTime;
    }
}
