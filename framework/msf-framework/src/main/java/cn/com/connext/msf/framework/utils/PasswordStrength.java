package cn.com.connext.msf.framework.utils;

public class PasswordStrength {

    /**
     * 密码强度
     *
     * @return Z = 字母 S = 数字 T = 特殊字符
     */
/*  一、假定密码字符数范围6-16，除英文数字和字母外的字符都视为特殊字符：
    0弱：^[0-9A-Za-z]{6,16}$
    1中：^(?=.{6,16})[0-9A-Za-z]*[^0-9A-Za-z][0-9A-Za-z]*$
    2强：^(?=.{6,16})([0-9A-Za-z]*[^0-9A-Za-z][0-9A-Za-z]*){2,}$
    二、假定密码字符数范围6-16，密码字符允许范围为ASCII码表字符：
    0弱：^[0-9A-Za-z]{6,16}$
    1中：^(?=.{6,16})[0-9A-Za-z]*[\x00-\x2f\x3A-\x40\x5B-\xFF][0-9A-Za-z]*$
    2强：^(?=.{6,16})([0-9A-Za-z]*[\x00-\x2F\x3A-\x40\x5B-\xFF][0-9A-Za-z]*){2,}$*/
    public static int check(String sourcePassword) {
        String regexZ = "\\d*";
        String regexS = "[a-zA-Z]+";
        String regexT = "\\W+$";
        String regexZT = "\\D*";
        String regexST = "[\\d\\W]*";
        String regexZS = "\\w*";
        String regexZST = "[\\w\\W]*";

        if (sourcePassword.matches(regexZ)) {
            return 0;
        }
        if (sourcePassword.matches(regexS)) {
            return 0;
        }
        if (sourcePassword.matches(regexT)) {
            return 0;
        }
        if (sourcePassword.matches(regexZT)) {
            return 1;
        }
        if (sourcePassword.matches(regexST)) {
            return 1;
        }
        if (sourcePassword.matches(regexZS)) {
            return 1;
        }
        if (sourcePassword.matches(regexZST)) {
            return 2;
        }
        return -1;
    }
}
