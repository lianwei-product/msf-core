package cn.com.connext.msf.framework.utils;

import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class Validator {
    /**
     * 简单验证手机号码（1开头，11位数字，不验证第二位，防止新出的号段验证不通过）
     */
    public static final String TEL_REGEX = "[1]\\d{10}";

    /**
     * 特殊字符
     */
    public static final List<String> ILLEGAL_CHARS = Lists.newArrayList("`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "+",
            "=", "\\", "[", "]", "{", "}", "|", ",", ".", "/", "<", ">", "?", ";", "'", ":", "\"",
            "·", "~", "！", "@", "#", "￥", "%", "……", "&", "*", "（", "）", "-", "——", "=", "+", "{", "}", "【", "】", "、", "|", "；", "：",
            "’", "‘", "“", "”", "，", "。", "、", "《", "》", "？");

    public static boolean isEmpty(Object object) {
        return StringUtils.isEmpty(object);
    }

    public static boolean isUUID(String string) {
        if (isEmpty(string))
            return false;
        if (string.length() != 36)
            return false;

        try {
            UUID.fromString(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNotUUID(String string) {
        return !isUUID(string);
    }

    public static boolean isOutLimit(String string, int maxLen) {
        return !isEmpty(string) && string.length() > maxLen;
    }

    public static boolean isNumber(String source) {
        try {
            new BigDecimal(source).toString();
        } catch (Exception e) {
            //异常 说明包含非数字。
            return false;
        }
        return true;

    }

    public static boolean isMobilePhone(String phone) {
        return !isEmpty(phone) && phone.matches(TEL_REGEX);
    }

    public static boolean isShort(String value) {
        try {
            Short.parseShort(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDate(String value) {
        try {
            Time.parseLocalDateTime(value, TimeZone.getDefault().getID());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isUrlString(String value) {
        if (StringUtils.isEmpty(value))
            return false;
        return value.matches("[a-z0-9_]*");
    }

    public static boolean isNotUrlString(String value) {
        return !isUrlString(value);
    }

    public static boolean isBoolean(String value) {
        try {
            Boolean.parseBoolean(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isIllegalStart(String value) {
        if (isEmpty(value)) {
            return false;
        }
        return ILLEGAL_CHARS.contains(String.valueOf(value.charAt(0)));
    }

}
