package cn.com.connext.msf.apimetrics.filter;

import cn.com.connext.msf.framework.metrics.RequestTraceVariables;
import cn.com.connext.msf.framework.utils.Base58UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(10)
@WebFilter(filterName = "ServletPreFilter", urlPatterns = "/api/*")
public class ServletPreFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletRequest.setAttribute(RequestTraceVariables.REQUEST_BEGIN_TIME, System.currentTimeMillis());
        String traceId = (String) httpServletRequest.getAttribute(RequestTraceVariables.TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = Base58UUID.newBase58UUID();
        }
        httpServletRequest.setAttribute(RequestTraceVariables.TRACE_ID, traceId);
        httpServletRequest.setAttribute(RequestTraceVariables.SPAN_ID, traceId);
        httpServletRequest.setAttribute(RequestTraceVariables.PARENT_SPAN_ID, traceId);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {
    }

}
