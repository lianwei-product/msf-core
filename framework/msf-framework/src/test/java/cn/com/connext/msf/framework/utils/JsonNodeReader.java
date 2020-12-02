package cn.com.connext.msf.framework.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class JsonNodeReader {
    public static List<String> read(ObjectNode objectNode, String sourceFieldName) {
        List<String> valueList = Lists.newArrayList();
        String[] fieldNames = StringUtils.split(sourceFieldName, ".");
        List<JsonNode> nodeList = Collections.singletonList(objectNode);
        for (String fieldName : fieldNames) {
            nodeList = getChildNode(nodeList, fieldName);
        }

        nodeList.forEach(node -> {
            valueList.add(node.asText(null));
        });
        return valueList;
    }



    private static List<JsonNode> getChildNode(List<JsonNode> nodeList, String fieldName) {
        List<JsonNode> childNodeList = Lists.newArrayList();
        for (JsonNode node : nodeList) {
            JsonNode childNode = node.get(fieldName);

            if (childNode == null) {
                continue;
            }

            if (childNode.isArray()) {
                for (JsonNode tmpNode : childNode) {
                    childNodeList.add(tmpNode);
                }
            } else {
                childNodeList.add(childNode);
            }
        }
        return childNodeList;
    }
}
