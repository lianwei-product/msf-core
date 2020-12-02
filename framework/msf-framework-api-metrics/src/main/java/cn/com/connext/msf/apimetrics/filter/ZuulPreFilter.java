package cn.com.connext.msf.apimetrics.filter;

import cn.com.connext.msf.framework.metrics.RequestTraceVariables;
import cn.com.connext.msf.framework.utils.Base58UUID;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class ZuulPreFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.set(RequestTraceVariables.REQUEST_BEGIN_TIME, System.currentTimeMillis());
        String newTraceId = Base58UUID.newBase58UUID();
        requestContext.set(RequestTraceVariables.TRACE_ID, newTraceId);
        requestContext.set(RequestTraceVariables.SPAN_ID, newTraceId);
        requestContext.set(RequestTraceVariables.PARENT_SPAN_ID, newTraceId);
        return null;
    }

}
