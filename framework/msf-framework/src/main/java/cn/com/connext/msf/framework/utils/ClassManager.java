package cn.com.connext.msf.framework.utils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ClassManager {

    private final static Map<String, Map<String, Field>> CLASS_FIELD_MAP = new HashMap<>();
    private final static Object LOCK_FLAG = new Object();

    public static Object getFieldValue(Object object, String fieldName) {
        if (object == null) return null;
        Class clazz = object.getClass();
        Map<String, Field> fieldMap = getClassFields(clazz);
        Field field = fieldMap.get(fieldName);
        if (field == null) {
            throw new RuntimeException(MessageFormat.format("Can not find {0} in {1}.", fieldName, clazz.getName()));
        }

        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(MessageFormat.format("get field {0} value error.", fieldName), e);
        }
    }

    private static Map<String, Field> getClassFields(Class clazz) {
        String className = clazz.getName();
        Map<String, Field> fieldMap = CLASS_FIELD_MAP.get(className);
        if (fieldMap == null) {
            synchronized (LOCK_FLAG) {
                fieldMap = CLASS_FIELD_MAP.get(className);
                if (fieldMap == null) {
                    fieldMap = new HashMap<>();
                    Field[] fields = clazz.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        field.setAccessible(true);
                        fieldMap.put(field.getName(), field);
                    }
                    CLASS_FIELD_MAP.put(className, fieldMap);
                }
            }
        }
        return fieldMap;
    }
}
