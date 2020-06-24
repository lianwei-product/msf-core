package cn.com.connext.msf.framework.query;

import java.util.Date;
import java.util.List;

public class Scroll<T> {
    private String scrollId;
    private Date expires;
    private long totalElements;
    private List<T> content;

    public Scroll(String scrollId, Date expires, long totalElements, List<T> content) {
        this.scrollId = scrollId;
        this.expires = expires;
        this.totalElements = totalElements;
        this.content = content;
    }

    public boolean isEnd() {
        return content.size() == 0;
    }

    public String getScrollId() {
        return scrollId;
    }

    public Date getExpires() {
        return expires;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<T> getContent() {
        return content;
    }
}
