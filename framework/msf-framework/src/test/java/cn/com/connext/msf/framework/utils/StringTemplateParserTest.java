package cn.com.connext.msf.framework.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class StringTemplateParserTest {
    @Test
    public void parseTest() throws Exception {
        String template = "customerName eq {member.user_name} AND phone eq {member.mobile}";
        String customeTemplate = "customerName eq [member.user_name] AND phone eq [member.mobile]";
        final String customeRegex = "\\[[a-zA-Z0-9._]*\\]";

        {
            Map<String, String> memberNode = Maps.newHashMap();
            memberNode.put("member.user_name", "Mike");
            memberNode.put("member.mobile", "13776510001");

            String result = StringTemplateParser.parse(template, memberNode);
            System.out.println(result);
            Assert.assertEquals("customerName eq Mike AND phone eq 13776510001", result);
        }

        {
            Map<String, String> memberNode = Maps.newHashMap();
            memberNode.put("member.user_name", "Mike");
            memberNode.put("member.mobile", "13776510001");

            String result = StringTemplateParser.parse(customeRegex, customeTemplate, memberNode);
            System.out.println(result);
            Assert.assertEquals("customerName eq Mike AND phone eq 13776510001", result);
        }

        {
            ObjectNode memberNode = JsonNodeFactory.instance.objectNode();
            memberNode.put("user_name", "Mike");
            memberNode.put("mobile", "13776510001");
            ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
            objectNode.set("member", memberNode);

            String result = StringTemplateParser.parse(template, objectNode);
            System.out.println(result);
            Assert.assertEquals("customerName eq Mike AND phone eq 13776510001", result);
        }

        {
            ObjectNode memberNode = JsonNodeFactory.instance.objectNode();
            memberNode.put("user_name", "Mike");
            memberNode.put("mobile", "13776510001");
            ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
            objectNode.set("member", memberNode);

            String result = StringTemplateParser.parse(customeRegex, customeTemplate, objectNode);
            System.out.println(result);
            Assert.assertEquals("customerName eq Mike AND phone eq 13776510001", result);
        }
    }

    @Test
    public void getKeysTest() throws Exception {
        String template = "customerName eq {member.user_name} AND phone eq {member.mobile}";
        String customeTemplate = "customerName eq [member.user_name] AND phone eq [member.mobile]";
        final String customeRegex = "\\[[a-zA-Z0-9._]*\\]";
        {
            List<String> keys = StringTemplateParser.getKeys(template);
            System.out.println(keys);
            Assert.assertEquals(2, keys.size());
        }
        {
            List<String> keys = StringTemplateParser.getKeys(customeRegex, customeTemplate);
            System.out.println(keys);
            Assert.assertEquals(2, keys.size());
        }
    }

}

