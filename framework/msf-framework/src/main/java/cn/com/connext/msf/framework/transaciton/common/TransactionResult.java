package cn.com.connext.msf.framework.transaciton.common;

import cn.com.connext.msf.framework.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResult {

    private final static String SUCCESS_CODE = "0";
    private final static String SUCCESS_MESSAGE = "success";

    /**
     * 事务结果编码
     */
    protected String code;

    /**
     * 事务异常消息
     */
    protected String message;

    /**
     * 是否为系统级别异常
     */
    protected boolean system;


    public TransactionResult() {

    }

    public final static TransactionResult SUCCESS = from(SUCCESS_CODE, SUCCESS_MESSAGE);

    public static TransactionResult from(String code, String message) {
        return from(code, message, false);
    }

    public static TransactionResult from(String code, String message, boolean system) {
        TransactionResult result = new TransactionResult();
        result.code = code;
        result.message = message;
        result.system = system;
        return result;
    }

    public void validate() {
        if (error()) {
            if (system) {
                throw new RuntimeException(message);
            } else {
                throw new BusinessException(code, message);
            }
        }
    }

    @JsonIgnore
    public boolean error() {
        return !SUCCESS_CODE.equals(code);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSystem() {
        return system;
    }

}
