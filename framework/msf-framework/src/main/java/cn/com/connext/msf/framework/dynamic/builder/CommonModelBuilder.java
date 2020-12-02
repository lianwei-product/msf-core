package cn.com.connext.msf.framework.dynamic.builder;

import cn.com.connext.msf.framework.dynamic.CommonModelField;
import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import cn.com.connext.msf.framework.dynamic.CommonModel;
import cn.com.connext.msf.framework.utils.Base58UUID;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CommonModelBuilder extends DynamicModelBuilder {

    private final Class<?> entityClass;

    public CommonModelBuilder(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public static CommonModel build(Class<?> entityClass) {
        return new CommonModelBuilder(entityClass).build();
    }

    public CommonModel build() {
        CommonModel model = new CommonModel();
        model.setId(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()));
        model.setName(entityClass.getSimpleName());
        model.setAliasName(entityClass.getSimpleName());

        appendModelFields(model.getFields(), entityClass);

        return model;
    }

    private void appendModelFields(List<CommonModelField> modelFieldList, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            CommonModelField modelField = new CommonModelField();
            modelField.setName(getName(field));
            modelField.setAliasName(getAliasName(field));
            modelField.setArrayType(isArray(field));
            modelField.setAllowNull(isAllowNull(field));
            modelField.setDefaultValue(getDefault(field));

            Class<?> fieldClass = getFieldClass(field);
            DynamicModelFieldType modelFieldType = getModelFieldType(fieldClass);
            modelField.setType(modelFieldType);

            if (modelFieldType == DynamicModelFieldType.NESTED) {
                modelField.setFields(Lists.newArrayList());
                appendModelFields(modelField.getFields(), fieldClass);
            }

            modelFieldList.add(modelField);
        }
    }

    public static CommonModel build(Builder builder) {
        return builder.build();
    }

    public static class Builder {

        private String id;
        private String name;
        private String aliasName;
        private List<CommonModelField> fields;

        public Builder(String id) {
            if (StringUtils.isEmpty(id)) {
                this.id = Base58UUID.newBase58UUID();
            } else {
                this.id = id;
            }
            fields = Lists.newArrayList();
        }

        public Builder() {
            this.id = Base58UUID.newBase58UUID();
            fields = Lists.newArrayList();
        }

        public Builder setModelField(List<CommonModelField> fields) {
            this.fields = fields;
            return this;
        }

        public Builder addModelField(CommonModelField field) {
            this.fields.add(field);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAliasName(String aliasName) {
            this.aliasName = aliasName;
            return this;
        }

        private CommonModel build() {
            CommonModel commonModel = new CommonModel();
            commonModel.setFields(this.fields);
            commonModel.setAliasName(this.aliasName);
            commonModel.setName(this.name);
            commonModel.setId(this.id);
            return commonModel;
        }
    }
}
