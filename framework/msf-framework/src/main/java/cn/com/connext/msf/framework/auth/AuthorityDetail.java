package cn.com.connext.msf.framework.auth;

/**
 * 授权信息详情
 * 用于控制授权细节，如 SCRM标准版本证书，公众号数量 {code:account-count, name: 可接入公众号数量, value: "5"}
 */
public class AuthorityDetail {
    private String code;
    private String name;
    private String value;


    public static AuthorityDetail from(String code, String name, String value) {
        AuthorityDetail detail = new AuthorityDetail();
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
