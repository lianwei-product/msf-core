package cn.com.connext.msf.webapp.query;

import cn.com.connext.msf.framework.query.QueryInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class QueryInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_FILTER_PARAMETER = "expression";
    private static final String DEFAULT_FIELDS_PARAMETER = "fields";
    private static final String DEFAULT_SCROLL_SIZE_PARAMETER = "scroll_size";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(QueryInfo.class);
    }

    @Override
    public QueryInfo resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                     NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
        Pageable pageable = pageableHandlerMethodArgumentResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setPageable(pageable);
        queryInfo.setExpression(webRequest.getParameter(DEFAULT_FILTER_PARAMETER));
        queryInfo.setFields(webRequest.getParameter(DEFAULT_FIELDS_PARAMETER));
        queryInfo.setScrollSize(getScrollSize(webRequest));
        queryInfo.setCheckOperator(true);
        return queryInfo;
    }

    private int getScrollSize(NativeWebRequest webRequest) {
        String sizeString = webRequest.getParameter(DEFAULT_SCROLL_SIZE_PARAMETER);
        if (StringUtils.isEmpty(sizeString)) return 0;
        try {
            return Integer.parseInt(sizeString);
        } catch (Exception e) {
            return 0;
        }
    }

}
