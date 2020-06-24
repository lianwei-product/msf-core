package cn.com.connext.msf.data.mongo.entity;

import cn.com.connext.msf.data.mongo.ShardDocument;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * 交易流水号历史记录
 */
@ShardDocument(collection = "request_limiting")
public class RequestLimiting {

    /**
     * 会员编号_交易场景（ORDER or REFUND）
     */
    @Id
    private String id;

    /**
     * 过期时间，到达到期时间后，MongoDB会主动删除该记录
     */
    @Indexed(expireAfterSeconds = 0)
    private Date expires;

    private RequestLimiting() {
    }

    public static RequestLimiting from(String customerId, String type, int expire) {
        RequestLimiting requestLimiting = new RequestLimiting();
        requestLimiting.id = customerId.concat("_").concat(type);
        requestLimiting.expires = DateUtils.addSeconds(new Date(), expire); // mongo缓存5秒
        return requestLimiting;
    }

    public String getId() {
        return id;
    }

    public Date getExpires() {
        return expires;
    }
}