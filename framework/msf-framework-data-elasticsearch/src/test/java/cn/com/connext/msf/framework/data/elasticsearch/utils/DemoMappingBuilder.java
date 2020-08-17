package cn.com.connext.msf.framework.data.elasticsearch.utils;

import cn.com.connext.msf.framework.data.elasticsearch.config.ConnextElasticSearchFeature;
import cn.com.connext.msf.framework.utils.JSON;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpHost;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoMappingBuilder {

    private final Logger logger = LoggerFactory.getLogger(DemoMappingBuilder.class);
    private final JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    public static String build() {
        DemoMappingBuilder builder = new DemoMappingBuilder();
        return builder.buildMapping();
    }

    public static String build2() {
        DemoMappingBuilder builder = new DemoMappingBuilder();
        return builder.buildMapping2();
    }

    @Test
    public void test() {
        String mapping = DemoMappingBuilder.build();
        logger.info("mapping:\n{}", mapping);
    }

    @Test
    public void test01() {
        HttpHost httpHost = new HttpHost("192.168.0.10", 9200);
        logger.info(httpHost.toURI());
    }

    private String buildMapping() {
        ObjectNode docNode = jsonNodeFactory.objectNode();
        ObjectNode properties = jsonNodeFactory.objectNode();
        ObjectNode fieldNode = jsonNodeFactory.objectNode();

        properties.set("properties", fieldNode);

        switch (ConnextElasticSearchFeature.getPrimaryVersion()) {
            case 5:
                docNode.set("doc", properties);
                break;
            case 6:
                docNode.set("_doc", properties);
                break;
            default:
                docNode = properties;
                break;
        }

        ObjectNode memberId = jsonNodeFactory.objectNode();
        memberId.put("type", "keyword");
        fieldNode.set("memberId", memberId);

        ObjectNode realName = jsonNodeFactory.objectNode();
        realName.put("type", "text");
        fieldNode.set("realName", realName);

        ObjectNode gender = jsonNodeFactory.objectNode();
        gender.put("type", "keyword");
        fieldNode.set("gender", gender);

        ObjectNode mobile = jsonNodeFactory.objectNode();
        mobile.put("type", "keyword");
        fieldNode.set("mobile", mobile);

        ObjectNode birthDate = jsonNodeFactory.objectNode();
        birthDate.put("type", "date");
        birthDate.put("format", "date_optional_time||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
        fieldNode.set("time", birthDate);

        ObjectNode province = jsonNodeFactory.objectNode();
        province.put("type", "keyword");
        fieldNode.set("province", province);

        ObjectNode city = jsonNodeFactory.objectNode();
        city.put("type", "keyword");
        fieldNode.set("city", city);

        ObjectNode points = jsonNodeFactory.objectNode();
        points.put("type", "double");
        fieldNode.set("points", points);

        ObjectNode cdpTag = jsonNodeFactory.objectNode();
        cdpTag.put("type", "keyword");
        fieldNode.set("cdp_tag", cdpTag);

        return JSON.toIndentJsonString(docNode);
    }

    private String buildMapping2() {
        ObjectNode docNode = jsonNodeFactory.objectNode();
        ObjectNode properties = jsonNodeFactory.objectNode();
        ObjectNode fieldNode = jsonNodeFactory.objectNode();

        properties.set("properties", fieldNode);

        switch (ConnextElasticSearchFeature.getPrimaryVersion()) {
            case 5:
                docNode.set("doc", properties);
                break;
            case 6:
                docNode.set("_doc", properties);
                break;
            default:
                docNode = properties;
                break;
        }

        ObjectNode memberId = jsonNodeFactory.objectNode();
        memberId.put("type", "keyword");
        fieldNode.set("memberId", memberId);

        ObjectNode realName = jsonNodeFactory.objectNode();
        realName.put("type", "keyword");
        fieldNode.set("realName", realName);

        ObjectNode gender = jsonNodeFactory.objectNode();
        gender.put("type", "keyword");
        fieldNode.set("gender", gender);

        ObjectNode points = jsonNodeFactory.objectNode();
        points.put("type", "keyword");
        fieldNode.set("points", points);

        return JSON.toIndentJsonString(docNode);
    }
}
