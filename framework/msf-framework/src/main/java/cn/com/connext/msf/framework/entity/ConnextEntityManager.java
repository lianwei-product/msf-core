package cn.com.connext.msf.framework.entity;

import cn.com.connext.msf.framework.annotation.Nesting;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConnextEntityManager {
    private final static ConcurrentHashMap<String, ConnextEntityInfo> CONNEXT_ENTITY_INFO_MAP;
    private static boolean SQL_SUPPORT = false;
    private static boolean NOSQL_SUPPORT = false;

    static {
        CONNEXT_ENTITY_INFO_MAP = new ConcurrentHashMap<>();
        try {
            SQL_SUPPORT = Class.forName("javax.persistence.Id") != null;
            NOSQL_SUPPORT = Class.forName("org.springframework.data.annotation.Id") != null;
        } catch (ClassNotFoundException e) {
            // do nothing.
        }
    }

    public static void put(Class clazz, ConnextEntityInfo connextEntityInfo) {
        String clazzName = clazz.getName();
        CONNEXT_ENTITY_INFO_MAP.put(clazzName, connextEntityInfo);
    }

    public static void put(String clazzName, ConnextEntityInfo connextEntityInfo) {
        CONNEXT_ENTITY_INFO_MAP.put(clazzName, connextEntityInfo);
    }

    public static ConnextEntityInfo getConnextEntityInfo(Class clazz) {
        String clazzName = clazz.getName();
        ConnextEntityInfo connextEntityInfo = CONNEXT_ENTITY_INFO_MAP.get(clazzName);
        List<String> deep = new ArrayList<>();
        try {
            if (connextEntityInfo == null) {
                connextEntityInfo = new ConnextEntityInfo();
                findInfo(connextEntityInfo, null, clazz, deep);

                CONNEXT_ENTITY_INFO_MAP.put(clazzName, connextEntityInfo);
            }
        } catch (Exception e) {
            throw new RuntimeException("构造查询模型失败", e);
        }
//        System.out.println(JSON.toJsonString(connextEntityInfo));
        return connextEntityInfo;
    }

    public static void findInfo(ConnextEntityInfo connextEntityInfo, String parentName, Class propertyClass, List<String> deep) throws Exception {
        String pre = "";
        if (StringUtils.isNotBlank(parentName)) {
            pre = parentName + ".";
        }

        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(propertyClass.getDeclaredFields()));
        Class superClass = propertyClass.getSuperclass();
        while (superClass != null) {
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
            superClass = superClass.getSuperclass();
        }

        for (Field field : fields) {
            deep.add(field.getName());
            checkLoop(deep);
            if (isNesting(field)) {
                // 嵌套结构
                if (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType())) {
                    // 处理数组
                    Type type = field.getGenericType();
                    if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
                        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                        String name = types[0].getTypeName();
                        findInfo(connextEntityInfo, pre + field.getName(), Class.forName(name), deep);
                    }
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    // 处理集合

                } else {
                    // 处理自定义类
                    findInfo(connextEntityInfo, pre + field.getName(), field.getType(), deep);
                }
            } else {
                // 判断类型，约定如果包含的是自定义的其他类那么继续做深度遍历
                ConnextEntityFieldInfo fieldInfo = ConnextEntityFieldInfo.from(parentName, field, SQL_SUPPORT, NOSQL_SUPPORT);
                connextEntityInfo.addField(fieldInfo);
            }
            deep.remove(deep.size() - 1);
        }
    }

    private static void checkLoop(List<String> fields) {
        String last = fields.get(fields.size() - 1);

        for (int i = fields.size() - 2; i >= 0; i--) {
            if (fields.get(i).equals(last)) {
                throw new RuntimeException("属性"+last+"循环嵌套");
            }
        }
    }

    private static boolean isNesting(Field field) {
        return field.getAnnotation(Nesting.class) != null;
    }
}
