package cn.com.connext.msf.framework.query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class QueryOperators {
    public final static String EQ = "eq";
    public final static String NE = "ne";
    public final static String LIKE = "like";
    public final static String IN = "in";
    public final static String NIN = "nin";
    public final static String GT = "gt";
    public final static String GE = "ge";
    public final static String LT = "lt";
    public final static String LE = "le";

    /**
     * 相对时间早于（LE），仅可用于时间类型字段
     */
    public final static String BF = "bf";

    /**
     * 相对时间早于晚于(GE)，仅可用于时间类型字段
     */
    public final static String AF = "af";

    /**
     * 月份中的上旬、下旬，仅可用于时间类型字段
     */
    public final static String MONTH_PERIOD = "monthPeriod";

    /**
     * 时间范围限定（月、日）,如生日（当月，两个月后，当日，十五日后）
     */
    public final static String DATE_RANGE = "dateRange";

    public final static String EMPTY = "empty";

    public final static Set<String> KEYWORD_OPERATORS = new HashSet<>(Arrays.asList(EQ, NE, EMPTY));
    public final static Set<String> TEXT_OPERATORS = new HashSet<>(Arrays.asList(EQ, NE, LIKE, EMPTY));
    public final static Set<String> NUMBER_OPERATORS = new HashSet<>(Arrays.asList(EQ, NE, GT, GE, LT, LE, EMPTY));
    public final static Set<String> DATE_OPERATORS = new HashSet<>(Arrays.asList(EQ, NE, GT, GE, LT, LE, BF, AF, MONTH_PERIOD, DATE_RANGE, EMPTY));
}
