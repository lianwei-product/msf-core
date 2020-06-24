package cn.com.connext.msf.framework.utils;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class PageUtilsTest {

    @Test
    public void toPage() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4);
        Page<Integer> page = PageUtils.toPage(list, PageRequest.of(0, 5));
        System.out.println(JSON.toIndentJsonString(page));
    }
}