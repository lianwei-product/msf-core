package cn.com.connext.msf.webapp.config;

public class WebApplicationExceptionMessage {

    private String code;
    private String message;

    public static WebApplicationExceptionMessage from(String code, String message) {
        WebApplicationExceptionMessage exceptionMessage = new WebApplicationExceptionMessage();
        exceptionMessage.code = code;
        exceptionMessage.message = message;
        return exceptionMessage;
    }

    public static WebApplicationExceptionMessage SERVER_BUSY = from("SYSTEM", "服务器正忙，请稍候重试。");

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
