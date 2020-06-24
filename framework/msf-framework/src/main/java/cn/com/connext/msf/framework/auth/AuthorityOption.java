package cn.com.connext.msf.framework.auth;

import java.util.Objects;

public class AuthorityOption {
    private String code;
    private String name;

    public static AuthorityOption from(String code, String name) {
        AuthorityOption authorityOption = new AuthorityOption();
        authorityOption.code = code;
        authorityOption.name = name;
        return authorityOption;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "code: " + code + ", name: " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthorityOption)) {
            return false;
        }
        AuthorityOption authorityOption = (AuthorityOption) obj;
        return Objects.equals(code, authorityOption.getCode());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

}
