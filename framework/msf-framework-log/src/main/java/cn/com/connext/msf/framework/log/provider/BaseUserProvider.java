package cn.com.connext.msf.framework.log.provider;

public class BaseUserProvider implements UserProvider {

    @Override
    public String findName(String userId) {
        return userId;
    }
}

