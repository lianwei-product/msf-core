package cn.com.connext.msf.framework.mapping.model;

import com.fasterxml.jackson.databind.JsonNode;

public class CurrentArray {
    private JsonNode array = null;
    private String prefix = "";

    public static CurrentArray from(JsonNode array, String prefix) {
        CurrentArray currentArray = new CurrentArray();
        currentArray.array = array;
        currentArray.prefix = prefix;
        return currentArray;
    }

    public JsonNode getArray() {
        return array;
    }

    public String getPrefix() {
        return prefix;
    }
}
