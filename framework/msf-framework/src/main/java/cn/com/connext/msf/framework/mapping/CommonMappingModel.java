package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.dynamic.CommonModelInfo;
import cn.com.connext.msf.framework.dynamic.DynamicModel;
import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommonMappingModel<S extends CommonMappingModelField> extends CommonModelInfo implements DynamicModel<S> {
    public List<S> fields;

    public CommonMappingModel() {
        this.fields = Lists.newArrayList();
    }

    @Override
    public List<S> getFields() {
        return fields;
    }

    public void setFields(List<S> fields) {
        this.fields = fields;
    }

    public S addField(Supplier<S> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull, Consumer<S> consumer) {
        return addField(supplier, name, aliasName, type, arrayType, defaultValue, allowNull, null, consumer);
    }

    public S addField(Supplier<S> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull) {
        return addField(supplier, name, aliasName, type, arrayType, defaultValue, allowNull, null, null);
    }

    public S addField(Supplier<S> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, Consumer<S> consumer) {
        return addField(supplier, name, aliasName, type, arrayType, defaultValue, true, null, consumer);
    }

    public S addField(Supplier<S> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, Consumer<S> consumer) {
        return addField(supplier, name, aliasName, type, arrayType, "", true, null, consumer);
    }

    public S addField(Supplier<S> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType) {
        return addField(supplier, name, aliasName, type, arrayType, "", true, null, null);
    }

    public S addField(Supplier<S> supplier, String name, String aliasName, DynamicModelFieldType type, Consumer<S> consumer) {
        return addField(supplier, name, aliasName, type, false, "", true, null, consumer);
    }

    public S addField(Supplier<S> supplier, String name, String aliasName, Consumer<S> consumer) {
        return addField(supplier, name, aliasName, DynamicModelFieldType.KEYWORD, false, "", true, null, consumer);
    }

    public S addField(Supplier<S> supplier, String name, String aliasName, DynamicModelFieldType type, boolean arrayType, String defaultValue, boolean allowNull, List<S> fieldList, Consumer<S> consumer) {
        S field = CommonMappingModelField.from(supplier, name, aliasName, type, arrayType, defaultValue, allowNull, fieldList, consumer);
        getFields().add(field);

        return field;
    }

    public S addField(S field, Consumer<S> consumer) {
        if (null != field) {
            if (null != consumer) {
                consumer.accept(field);
            }
            getFields().add(field);
        }
        return field;
    }
}
