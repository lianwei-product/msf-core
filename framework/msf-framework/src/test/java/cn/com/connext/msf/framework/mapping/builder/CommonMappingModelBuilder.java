package cn.com.connext.msf.framework.mapping.builder;

import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import cn.com.connext.msf.framework.dynamic.builder.DynamicModelBuilder;
import cn.com.connext.msf.framework.mapping.CommonMappingModel;
import cn.com.connext.msf.framework.mapping.CommonMappingModelField;
import cn.com.connext.msf.framework.mapping.entity.CdpDataModelField;
import cn.com.connext.msf.framework.utils.Base58UUID;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CommonMappingModelBuilder extends DynamicModelBuilder {

    private final Class<?> entityClass;

    public CommonMappingModelBuilder(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public static CommonMappingModel build(Class<?> entityClass) {
        return new CommonMappingModelBuilder(entityClass).build();
    }

    public CommonMappingModel build() {
        CommonMappingModel model = new CommonMappingModel();
        model.setId(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()));
        model.setName(entityClass.getSimpleName());
        model.setAliasName(entityClass.getSimpleName());

        appendModelFields(model.getFields(), entityClass);

        return model;
    }

    private void appendModelFields(List<CommonMappingModelField> modelFieldList, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            CommonMappingModelField modelField = new CommonMappingModelField();
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

    public static CommonMappingModel build(Builder builder) {
        return builder.build();
    }

    public static class Builder {

        private String id;
        private String name;
        private String aliasName;
        private List<CommonMappingModelField> fields;

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

        public Builder setModelField(List<CommonMappingModelField> fields) {
            this.fields = fields;
            return this;
        }

        public Builder addModelField(CdpDataModelField field) {
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

        private CommonMappingModel build() {
            CommonMappingModel commonModel = new CommonMappingModel();
            commonModel.setFields(this.fields);
            commonModel.setAliasName(this.aliasName);
            commonModel.setName(this.name);
            commonModel.setId(this.id);
            return commonModel;
        }
    }

}
