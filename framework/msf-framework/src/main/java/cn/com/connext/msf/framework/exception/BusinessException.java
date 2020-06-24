package cn.com.connext.msf.framework.exception;

import java.text.MessageFormat;

public class BusinessException extends RuntimeException {

    private String code;
    private Object[] args;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Object... args) {
        super(MessageFormat.format(message, args));
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return this.code;
    }

    public Object[] getArgs() {
        return args;
    }
}
