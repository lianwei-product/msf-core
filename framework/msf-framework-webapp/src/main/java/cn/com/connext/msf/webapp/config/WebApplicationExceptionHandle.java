package cn.com.connext.msf.webapp.config;

import cn.com.connext.msf.framework.crypto.CRC32Encoder;
import cn.com.connext.msf.framework.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class WebApplicationExceptionHandle {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebApplicationExceptionHandle.class);

    private final MessageSource messageSource;

    public WebApplicationExceptionHandle(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseEntity<WebApplicationExceptionMessage> businessExceptionHandler(HttpServletRequest request, BusinessException e) throws Exception {
        WebApplicationExceptionMessage exceptionMessage = new WebApplicationExceptionMessage();
        if (StringUtils.isEmpty(e.getCode())) {
            exceptionMessage.setCode(StringUtils.isEmpty(e.getCode()) ? "SYSTEM" : e.getCode());
            exceptionMessage.setMessage(e.getMessage());
        } else {
            exceptionMessage.setCode(e.getCode());
            exceptionMessage.setMessage(messageSource.getMessage(e.getCode(), e.getArgs(), e.getMessage(), request.getLocale()));
        }
        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public ResponseEntity<WebApplicationExceptionMessage> defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Throwable e) throws Exception {
        WebApplicationExceptionMessage exceptionMessage = new WebApplicationExceptionMessage();
        exceptionMessage.setCode("SYSTEM");
        exceptionMessage.setMessage(e.getMessage());

        String exceptionType = e.getClass().getName();
        HttpStatus httpStatus;
        switch (exceptionType) {
            case "org.springframework.http.converter.HttpMessageNotReadableException":
                httpStatus = HttpStatus.BAD_REQUEST;
                exceptionMessage.setCode("SYSTEM");
                exceptionMessage.setMessage("Request body should be a JSON object");
                break;

            case "org.springframework.web.bind.MissingServletRequestParameterException":
                MissingServletRequestParameterException missingServletRequestParameterException = (MissingServletRequestParameterException) e;
                httpStatus = HttpStatus.BAD_REQUEST;
                exceptionMessage.setCode("SYSTEM");
                exceptionMessage.setMessage(missingServletRequestParameterException.getMessage());
                break;

            case "org.springframework.web.HttpRequestMethodNotSupportedException":
                httpStatus = HttpStatus.BAD_REQUEST;
                exceptionMessage.setCode("SYSTEM");
                exceptionMessage.setMessage("Request method " + request.getMethod().toLowerCase() + " not supported for this url.");
                break;

            case "org.springframework.web.HttpMediaTypeNotSupportedException":
                httpStatus = HttpStatus.BAD_REQUEST;
                exceptionMessage.setCode("SYSTEM");
                exceptionMessage.setMessage("Request media type not supported for this url.");
                break;

            case "org.apache.catalina.connector.ClientAbortException":
                httpStatus = HttpStatus.BAD_REQUEST;
                exceptionMessage.setCode("SYSTEM");
                exceptionMessage.setMessage("Connection reset by peer.");
                break;

            default:
                httpStatus = HttpStatus.EXPECTATION_FAILED;
                String msg = e.getMessage();
                String code = Long.toString(CRC32Encoder.encode(msg));
                exceptionMessage.setCode(code);
                exceptionMessage.setMessage(WebApplicationExceptionMessage.SERVER_BUSY.getMessage());
                LOGGER.error("[".concat(code).concat("]"), e);
                break;
        }
        return new ResponseEntity<>(exceptionMessage, httpStatus);
    }


}
