package cn.com.connext.msf.framework.data.elasticsearch.utils;

import org.apache.commons.lang3.StringUtils;

public class NestedPath {

    public static boolean isNestedPath(String fieldName) {
        return fieldName.contains(".");
    }

    public static String getNestedPath(String fieldName) {
        StringBuilder path = new StringBuilder();
        if (fieldName.contains(".")) {
            String[] paths = StringUtils.split(fieldName, ".");

            for (int i = 0; i < paths.length - 1; i++) {
                path.append(paths[i]);
                if (i < paths.length - 2) {
                    path.append(".");
                }
            }
        }
        return path.toString();
    }
}
