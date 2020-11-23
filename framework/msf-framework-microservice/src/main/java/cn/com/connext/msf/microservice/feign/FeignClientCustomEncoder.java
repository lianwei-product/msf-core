package cn.com.connext.msf.microservice.feign;

import cn.com.connext.msf.framework.query.QueryInfo;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeignClientCustomEncoder implements Encoder {

    private final Encoder delegate;

    FeignClientCustomEncoder(Encoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (object instanceof Pageable) {
            Pageable pageable = (Pageable) object;
            initPageableTemplate(template, pageable);
        } else if (object instanceof QueryInfo) {
            QueryInfo queryInfo = (QueryInfo) object;
            initPageableTemplate(template, queryInfo.getPageable());
            template.query("expression", queryInfo.getExpression());
            template.query("scroll_size", Integer.toString(queryInfo.getScrollSize()));
            template.query("fields", queryInfo.getFields());
        } else {
            delegate.encode(object, bodyType, template);
        }
    }

    private void initPageableTemplate(RequestTemplate template, Pageable pageable) {
        if (pageable != null) {
            template.query("page", pageable.getPageNumber() + "");
            template.query("size", pageable.getPageSize() + "");

            Collection<String> existingSorts = template.queries().get("sort");
            List<String> sortQueries = existingSorts != null ? new ArrayList<>(existingSorts) : new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                sortQueries.add(order.getProperty() + "," + order.getDirection());
            }
            template.query("sort", sortQueries);
        }
    }
}
