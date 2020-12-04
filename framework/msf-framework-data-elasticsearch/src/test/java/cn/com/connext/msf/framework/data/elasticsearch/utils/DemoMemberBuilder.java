package cn.com.connext.msf.framework.data.elasticsearch.utils;

import cn.com.connext.msf.framework.utils.StringUtils;
import cn.com.connext.msf.framework.utils.Time;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class DemoMemberBuilder {

    private final JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
    private final Random random = new Random();


    public ObjectNode build(int index) {
        String idString = StringUtils.padLeft(Integer.toString(index), 5, '0');
        ObjectNode member = jsonNodeFactory.objectNode();

        member.put("memberId", "member" + idString);
        member.put("realName", "name" + idString);
        member.put("gender", random.nextInt(2) == 0 ? "男" : "女");
        member.put("mobile", "139" + idString);
        member.put("birthDate", getBirthday());

        Region region = Region.buildRegion();
        member.put("province", region.getProvince());
        member.put("city", region.getCity());
        member.put("points", index);

        ArrayNode arrayNode = jsonNodeFactory.arrayNode();
        member.set("cdp_tag", arrayNode);

        return member;
    }

    public ObjectNode build2(int index) {
        String idString = StringUtils.padLeft(Integer.toString(index), 5, '0');
        ObjectNode member = jsonNodeFactory.objectNode();

        member.put("memberId", "member" + idString);
        member.put("realName", "name" + idString);
        member.put("gender", random.nextInt(2) == 0 ? "男" : "女");
        member.put("points", index);
        return member;
    }

    private String getBirthday() {
        int currentYear = LocalDateTime.now().getYear();
        int maxAge = 60;
        int minAge = 20;
        String year = Integer.toString(currentYear - maxAge + random.nextInt(maxAge - minAge));
        int day = random.nextInt(365);

        Date date = Time.parseDate(year + "-01-01");
        date = DateUtils.addDays(date, day);
        return Time.getTimeFormatString(date, "yyyy-MM-dd") + "T00:00:00+08:00";
    }

    public ObjectNode buildUpdateNode(int points) {
        ObjectNode member = jsonNodeFactory.objectNode();
        member.put("points", points);
        return member;
    }
}
