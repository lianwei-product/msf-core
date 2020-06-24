package cn.com.connext.msf.framework.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConnextEntityInfo {
    private Map<String, ConnextEntityFieldInfo> fields;

    public ConnextEntityInfo() {
        fields = new HashMap<>();
    }

    public void addField(String name, String type, boolean allowSort, Set<String> operators) {
        fields.put(name, ConnextEntityFieldInfo.from(name, type, allowSort, operators));
    }

    public void addFieldList(List<ConnextEntityFieldInfo> fieldList) {
        fieldList.forEach(this::addField);
    }

    public void addField(ConnextEntityFieldInfo field) {
        fields.put(field.getName(), field);
    }

    public ConnextEntityFieldInfo getField(String fieldName) {
        return fields.get(fieldName);
    }

    public Map<String, ConnextEntityFieldInfo> getFields() {
        return fields;
    }
}
