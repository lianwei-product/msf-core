package cn.com.connext.msf.framework.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static CloseableHttpClient client;
    private static Charset encoding = Charset.forName("UTF-8");

    public static String get(String url) {
        String result = "";

        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");

        try {
            HttpResponse response = client.execute(get);
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            EntityUtils.consume(response.getEntity());
        } catch (Exception ex) {
            throw new RuntimeException("Http get request error: " + ex.getMessage(), ex);
        }finally {
            try {
                ((CloseableHttpClient) client).close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        return result;
    }

    @Autowired
    public void setHttpClient(CloseableHttpClient client) {
        HttpUtils.client = client;
    }

}
