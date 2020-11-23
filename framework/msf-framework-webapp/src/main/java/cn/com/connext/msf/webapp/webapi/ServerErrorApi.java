package cn.com.connext.msf.webapp.webapi;

import cn.com.connext.msf.webapp.annotation.ApiPath;
import cn.com.connext.msf.webapp.config.WebApplicationExceptionMessage;
import org.springframework.web.bind.annotation.GetMapping;

@ApiPath("/error")
public class ServerErrorApi {

    private final static WebApplicationExceptionMessage NOT_FOUND;

    static {
        NOT_FOUND = WebApplicationExceptionMessage.from("404", "Request url not found.");
    }

    @GetMapping("/404")
    public WebApplicationExceptionMessage notFound() {
        return NOT_FOUND;
    }

}
