package cn.com.connext.msf.framework.utils;

import cn.com.connext.msf.framework.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;

/**
 * @Author: ming.wang
 * @Date: 2020/9/1 14:52
 * @Description:
 */
public class ObjectNodeUtil {

    public static Object getObject(JsonNode node, String field, Class clazz) {
        if (clazz.equals(String.class)) {
        } else if (clazz.equals(Integer.class)) {
            return getInt(node, field);
        } else if (clazz.equals(Long.class)) {
            return getLong(node, field);
        } else if (clazz.equals(Float.class)) {
            return getDouble(node, field);
        } else if (clazz.equals(Double.class)) {
            return getDouble(node, field);
        } else if (clazz.equals(Boolean.class)) {
            return getBoolean(node, field);
        }
        return getString(node, field);
    }

    public static ObjectNode delNodeByField(ObjectNode node, String field) {
        try {
            ObjectNode tmp = null;
            if (!org.apache.commons.lang3.StringUtils.isEmpty(field)) {
                String[] split = field.split("\\.");
                int length = split.length;
                if (null != split && length > 0) {
                    if (length == 1) {
                        node.remove(field);
                        return node;
                    }
                    for (int i = 0; i < length; i++) {
                        if (null == tmp) {
                            tmp = (ObjectNode) node.get(split[i]);
                        } else {
                            tmp = (ObjectNode) tmp.get(split[i]);
                        }
                        if (i == length - 2) {
                            tmp.remove(split[length - 1]);
                            return node;
                        }
                    }
                }
            }
            return node;
        } catch (Exception e) {
            throw new BusinessException("del node by field error" + e);
        }
    }

    public static String getString(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.orElse(JsonNodeFactory.instance.objectNode()).asText();
    }

    public static Optional<String> getStringOp(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.map(x -> x.asText());
    }

    public static Integer getInt(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.orElse(JsonNodeFactory.instance.objectNode()).asInt();
    }

    public static Optional<Integer> getIntOp(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.map(x -> x.asInt());
    }

    public static Long getLong(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.orElse(JsonNodeFactory.instance.objectNode()).asLong();
    }

    public static Optional<Long> getLongOp(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.map(x -> x.asLong());
    }

    public static Double getDouble(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.orElse(JsonNodeFactory.instance.objectNode()).asDouble();
    }

    public static Optional<Double> getDoubleOp(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.map(x -> x.asDouble());
    }

    public static Boolean getBoolean(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.orElse(JsonNodeFactory.instance.objectNode()).asBoolean();
    }

    public static Optional<Boolean> getBooleanOp(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = commonOptional(node, field);
        return optionalJsonNode.map(x -> x.asBoolean());
    }

    public static boolean isNull(JsonNode node, String field) {
        return null == node || null == node.get(field) || node.get(field).isNull() || StringUtils.isEmpty(node.get(field).asText());
    }

    public static boolean isArrNull(JsonNode node, String field) {
        return null == node || null == node.get(field) || node.get(field).isNull();
    }

    public static LocalDate getLocalDate(JsonNode node, String field) {
        try {
            String string = getString(node, field);
            return Optional.ofNullable(Optional.ofNullable(string).map(x -> ZonedDateTime.parse(string)).orElse(null))
                    .map(x -> x.toLocalDate()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Optional<LocalDate> getLocalDateOp(JsonNode node, String field) {
        try {
            String string = getString(node, field);
            return Optional.ofNullable(Optional.ofNullable(Optional.ofNullable(string).map(x -> ZonedDateTime.parse(string)).orElse(null))
                    .map(x -> x.toLocalDate()).orElse(null));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static LocalDateTime getLocalDateTime(JsonNode node, String field) {

        try {
            String string = getString(node, field);
            return Optional.ofNullable(Optional.ofNullable(string).map(x -> ZonedDateTime.parse(string)).orElse(null))
                    .map(x -> x.toLocalDateTime()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Optional<LocalDateTime> getLocalDateTimeOp(JsonNode node, String field) {

        try {
            String string = getString(node, field);
            return Optional.ofNullable(Optional.ofNullable(Optional.ofNullable(string).map(x -> ZonedDateTime.parse(string)).orElse(null))
                    .map(x -> x.toLocalDateTime()).orElse(null));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static ZonedDateTime getZonedDateTime(JsonNode node, String field) {
        try {
            String string = getString(node, field);
            return Optional.ofNullable(string)
                    .map(x ->
                            {
                                try {
                                    return ZonedDateTime.parse(string);
                                } catch (Exception e) {
                                    return null;
                                }
                            }
                    ).orElseGet(() ->
                            ZonedDateTime.of(LocalDateTime.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ZoneId.systemDefault())
                    );
        } catch (Exception e) {
            return null;
        }

    }

    public static Optional<ZonedDateTime> getZonedDateTimeOp(JsonNode node, String field) {
        try {
            String string = getString(node, field);
            return Optional.ofNullable(Optional.ofNullable(string)
                    .map(x ->
                            {
                                try {
                                    return ZonedDateTime.parse(string);
                                } catch (Exception e) {
                                    return null;
                                }
                            }
                    ).orElseGet(() ->
                            ZonedDateTime.of(LocalDateTime.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ZoneId.systemDefault())
                    ));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    public static Iterator<JsonNode> getArr(JsonNode node, String field) {
        if ((!isArrNull(node, field)) && node.get(field).isArray()) {
            Iterator<JsonNode> iterator = node.get(field).iterator();
            return iterator;
        }
        return null;
    }

    public static Optional<JsonNode> commonOptional(JsonNode node, String field) {
        Optional<JsonNode> optionalJsonNode = Optional.ofNullable(node);
        if (!org.apache.commons.lang3.StringUtils.isEmpty(field)) {
            String[] split = field.split("\\.");
            if (null != split && split.length > 0) {
                for (String name : split) {
                    optionalJsonNode = optionalJsonNode.map(x -> x.get(name));
                }
            }
        }
        return optionalJsonNode;
    }

}
