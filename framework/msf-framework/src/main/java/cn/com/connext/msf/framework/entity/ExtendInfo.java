package cn.com.connext.msf.framework.entity;

/**
 * 实体扩展信息
 */
public class ExtendInfo {

    private String code;
    private String name;
    private String value;


    public static ExtendInfo from(String code, String name, String value) {
        ExtendInfo detail = new ExtendInfo();
        detail.code = code;
        detail.name = name;
        detail.value = value;
        return detail;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
