package cn.com.connext.msf.framework.file.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectNodeManager {

    public static String getFieldValue(Object objectNode, String fieldName) {
        JsonNode jsonNode = ((ObjectNode) objectNode).get(fieldName);

        return jsonNode == null ? "" : jsonNode.asText();
    }
}
