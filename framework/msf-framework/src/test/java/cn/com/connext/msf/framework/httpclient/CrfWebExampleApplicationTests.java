package cn.com.connext.msf.framework.httpclient;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CrfWebExampleApplicationTests {

    @Resource(name = "httpClientManagerFactoryBen")
    private CloseableHttpClient client;


    @Test
    public void contextLoads() throws Exception {

        HttpGet get = new HttpGet("https://www.baidu.com");
        CloseableHttpResponse response = client.execute(get);
        System.out.println("client object:" + client);
        HttpEntity entity = response.getEntity();
        System.out.println("============" + EntityUtils.toString(entity, Consts.UTF_8) + "=============");
        EntityUtils.consumeQuietly(entity);// 释放连接

    }

}
