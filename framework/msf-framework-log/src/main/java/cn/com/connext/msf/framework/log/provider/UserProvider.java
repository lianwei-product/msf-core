package cn.com.connext.msf.framework.log.provider;

public interface UserProvider<BaseUser> {

    String findName(String userId);
}

