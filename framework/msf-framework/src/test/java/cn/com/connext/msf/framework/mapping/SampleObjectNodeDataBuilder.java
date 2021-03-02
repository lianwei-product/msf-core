package cn.com.connext.msf.framework.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SampleObjectNodeDataBuilder {

    public static ObjectNode buildTestObjectNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode member = objectMapper.createObjectNode();
        member.put("id", "jd001");
        member.put("realName", "ChengHan");
        member.put("gender", "男");
        member.put("mobile", "13770655999");
        member.put("isok", true);

        ArrayNode telephones = objectMapper.createArrayNode();
        telephones.add("1377065599");
        telephones.add("025-52196543");
        member.set("telephones", telephones);

        member.put("birthDate", "1982-10-28");
        member.put("province", "jiangsu");
        member.put("city", "nanjing");
        member.put("activeScore", 10);
        member.put("description", "this field not need.");
        member.put("level", 1);
        member.put("experience", 999656);

        ArrayNode addressInfo = objectMapper.createArrayNode();
        ObjectNode address01 = objectMapper.createObjectNode();
        address01.put("type", "Home");
        address01.put("address", "JiangSu NanJing 5 road");
        address01.put("description", "this field not need.");
        addressInfo.add(address01);

        ObjectNode address02 = objectMapper.createObjectNode();
        address02.put("type", "Work");
        address02.put("address", "Shang Hai 5 road");
        address02.put("description", "this field not need.");
        addressInfo.add(address02);

        member.set("lastAddressInfo", address01);

        ArrayNode lastLoginTimes = objectMapper.createArrayNode();
        lastLoginTimes.add("2018-10-01 10:00:00");
        lastLoginTimes.add("2018-10-01T10:00:00+08:00");
        member.set("lastLoginTimes", lastLoginTimes);

        member.set("addressInfo", addressInfo);

        ObjectNode behaviorDetail = objectMapper.createObjectNode();
        behaviorDetail.put("source", "network");
        behaviorDetail.put("action", "register");

        ObjectNode behavior = objectMapper.createObjectNode();
        behavior.put("type", "wechat");
        behavior.set("detail", behaviorDetail);

        member.set("behavior", behavior);

        return member;
    }


    public static ObjectNode buildJDMember(int index) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode member = objectMapper.createObjectNode();
        String indexString = cn.com.connext.msf.framework.utils.StringUtils.padLeft(Integer.toString(index), 6, '0');
        member.put("id", "jd" + indexString);
        member.put("realName", "ChengHan");
        member.put("gender", "男");
        member.put("mobile", "T139" + indexString);
        member.put("birthDate", "1982-10-28T00:00:00+08:00");
        member.put("province", "jiangsu");
        member.put("city", "nanjing");

        ArrayNode addressInfo = objectMapper.createArrayNode();
        ObjectNode address01 = objectMapper.createObjectNode();
        address01.put("type", "Home");
        address01.put("address", "JiangSu NanJing 5 road");
        addressInfo.add(address01);

        member.set("addressInfo", addressInfo);
        member.put("updateTime", "2019-01-12 10:00:00");
        return member;
    }

    public static ObjectNode buildNestedDefaultValue01() {
        ObjectNode name = JsonNodeFactory.instance.objectNode();
        ObjectNode nameInfo = JsonNodeFactory.instance.objectNode();
        nameInfo.put("fullName", "ChengHan");
        nameInfo.put("firstName", "Han");
        nameInfo.put("lastName", "Cheng");
        name.set("name", nameInfo);
        return name;
    }

    public static ObjectNode buildNestedDefaultValue02() {
        ObjectNode name = JsonNodeFactory.instance.objectNode();
        ObjectNode nameInfo = JsonNodeFactory.instance.objectNode();
        nameInfo = JsonNodeFactory.instance.objectNode();
        nameInfo.put("firstName", "Han");
        nameInfo.put("lastName", "Cheng");
        name.set("name", nameInfo);
        return name;
    }

    public static ObjectNode buildNestedArrayDefaultValue01() {
        ObjectNode name = JsonNodeFactory.instance.objectNode();
        ObjectNode nameInfo = JsonNodeFactory.instance.objectNode();
        nameInfo.put("fullName", "ChengHan");
        name.set("name", nameInfo);
        return name;
    }

    public static ObjectNode buildNestedArrayDefaultValue02() {
        ObjectNode name = JsonNodeFactory.instance.objectNode();
        ObjectNode nameInfo = JsonNodeFactory.instance.objectNode();
        name.set("name", nameInfo);
        return name;
    }

    public static ObjectNode buildFixedValue() {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("amount", "NOT NUMBER");
        return objectNode;
    }

    public static ObjectNode buildDictConvert(int type) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();

        if (type == 0) {
            objectNode.put("gender_cn", "女");
        }
        if (type == 1) {
            objectNode.put("gender_cn", "男");
        }
        return objectNode;
    }

    public static ObjectNode buildMultiSourceFieldConvert01() {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("id", "member001");
        objectNode.put("name", "zhangsan");
        objectNode.put("province", "江苏省");
        objectNode.put("city", "南京市");
        objectNode.put("district", "玄武区");
        return objectNode;
    }

    public static ObjectNode buildConditionConvert01() {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        return objectNode;
    }

    public static ObjectNode buildConditionConvert02(String val) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("level", val);

        return objectNode;
    }

    public static ObjectNode buildDataModel() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode member = objectMapper.createObjectNode();
        member.put("customerNameSource", "jd001");
        member.put("realNameSource", "ChengHan");
        member.put("phoneSource", "13770655999");

        ObjectNode sourceIds = objectMapper.createObjectNode();
        sourceIds.put("cidSource", "0001");
        member.set("sourceIdsSource", sourceIds);

        return member;
    }

    public static ObjectNode buildConditionConvert03(String val) {
        ObjectNode name = JsonNodeFactory.instance.objectNode();
        ObjectNode nameInfo = JsonNodeFactory.instance.objectNode();
        nameInfo.put("level", val);
        name.set("name", nameInfo);
        return name;
    }

    public static ObjectNode buildConditionConvert04(String val1, String val2) {
        ObjectNode name = JsonNodeFactory.instance.objectNode();
        ObjectNode nameInfo = JsonNodeFactory.instance.objectNode();
        nameInfo.put("score1", val1);
        nameInfo.put("score2", val2);
        name.set("name", nameInfo);
        return name;
    }

    public static ObjectNode buildNestedMapping01() {
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayNode addressInfo = objectMapper.createArrayNode();
        ObjectNode item1 = JsonNodeFactory.instance.objectNode();
        item1.put("type", "Home");
        item1.put("province", "江蘇");
        item1.put("city", "南京");

        ObjectNode item2 = JsonNodeFactory.instance.objectNode();
        item2.put("type", "work");
        item2.put("province", "安徽");
        item2.put("city", "蕪湖");
        addressInfo.add(item1);
        addressInfo.add(item2);

        ObjectNode name = JsonNodeFactory.instance.objectNode();
        name.set("addressInfo", addressInfo);
        return name;
    }

    public static ObjectNode buildNestedMapping04() {
        ObjectNode item1 = JsonNodeFactory.instance.objectNode();
        item1.put("type", "Home");
        item1.put("province", "江蘇");
        item1.put("city", "南京");

        return item1;
    }

    public static ObjectNode buildNestedMapping06() {
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayNode addressInfo = objectMapper.createArrayNode();
        ObjectNode item1 = JsonNodeFactory.instance.objectNode();
        item1.put("type", "Home");
        item1.put("province", "江苏");
        ObjectNode city1 = JsonNodeFactory.instance.objectNode();
        ObjectNode town1 = JsonNodeFactory.instance.objectNode();
        town1.put("detail", "南京江宁区");
        city1.set("town", town1);
        item1.set("city", city1);

        ObjectNode item2 = JsonNodeFactory.instance.objectNode();
        item2.put("type", "work");
        item2.put("province", "安徽");
        ObjectNode city2 = JsonNodeFactory.instance.objectNode();
        ObjectNode town2 = JsonNodeFactory.instance.objectNode();
        town2.put("detail", "芜湖鸠江区");
        city2.set("town", town2);
        item2.set("city", city2);

        addressInfo.add(item1);
        addressInfo.add(item2);

        ObjectNode name = JsonNodeFactory.instance.objectNode();
        name.set("addressInfo", addressInfo);
        name.put("gender", "女");
        return name;
    }

    public static ObjectNode buildStr2Array() {
        ObjectNode item1 = JsonNodeFactory.instance.objectNode();
        item1.put("province", "江蘇省,浙江省");
        return item1;
    }
}