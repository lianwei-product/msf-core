package cn.com.connext.msf.framework.utils;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtils {

    public static <T> Page<T> toPage(List<T> list, Pageable pageable) {
        if (list == null) {
            return new PageImpl<>(Lists.newArrayList(), pageable, 0);
        }

        if (list.size() == 0) {
            return new PageImpl<>(list, pageable, 0);
        }

        int pos = (int) pageable.getOffset();
        if (pos > list.size()) {
            pageable = PageRequest.of((int) Math.ceil((double) list.size() / (double) pageable.getPageSize() - 1), pageable.getPageSize());
            pos = (int) pageable.getOffset();
        }

        int end = pos + pageable.getPageSize();
        if (end > list.size()) {
            end = list.size();
        }

        return new PageImpl<>(list.subList(pos, end), pageable, list.size());
    }

}
