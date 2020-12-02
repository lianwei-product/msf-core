package cn.com.connext.msf.framework.utils;

import cn.com.connext.msf.framework.query.QueryPage;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JSON {
    private static final ObjectMapper OBJECT_MAPPER;
    private static final ObjectMapper INDENT_OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        OBJECT_MAPPER.registerModule(new ParameterNamesModule());
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.registerModule(new JavaTimeModule()); // new module, NOT JSR310Module

        try {
            if (Class.forName("org.springframework.data.domain.Page") != null) {
                OBJECT_MAPPER.addMixIn(Page.class, PageMixIn.class);
            }
        } catch (Exception e) {
            // do nothing
        }

        INDENT_OBJECT_MAPPER = new ObjectMapper();
        INDENT_OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        INDENT_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        INDENT_OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        INDENT_OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        INDENT_OBJECT_MAPPER.registerModule(new ParameterNamesModule());
        INDENT_OBJECT_MAPPER.registerModule(new Jdk8Module());
        INDENT_OBJECT_MAPPER.registerModule(new JavaTimeModule()); // new module, NOT JSR310Module

        try {
            if (Class.forName("org.springframework.data.domain.Page") != null) {
                INDENT_OBJECT_MAPPER.addMixIn(Page.class, PageMixIn.class);
            }
        } catch (Exception e) {
            // do nothing
        }
    }

    public static ObjectNode createObjectNode() {
        return OBJECT_MAPPER.createObjectNode();
    }

    public static <T> ObjectNode toObjectNode(T t) {
        return JSON.parseObject(JSON.toJsonString(t), ObjectNode.class);
    }

    public static <T> ArrayNode toArrayNode(T t) {
        return JSON.parseObject(JSON.toJsonString(t), ArrayNode.class);
    }

    public static Map<String, Object> toMap(JsonNode jsonNode) {
        return OBJECT_MAPPER.convertValue(jsonNode, Map.class);
    }

    public static <T> T parseObject(JsonNode node, Class<T> valueType) {
        return JSON.parseObject(node.toString(), valueType);
    }

    public static <T> T parseObject(String json, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseList(String json, Class<T> valueType) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(ArrayList.class, valueType);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toIndentJsonString(Object object) {
        try {
            return INDENT_OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T clone(T source, Class<T> tClass) {
        return parseObject(toJsonString(source), tClass);
    }

    @JsonDeserialize(as = QueryPage.class)
    private interface PageMixIn {
    }
}

