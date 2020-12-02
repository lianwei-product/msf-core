package cn.com.connext.msf.framework.dynamic.builder;

import cn.com.connext.msf.framework.dynamic.CommonModel;
import cn.com.connext.msf.framework.dynamic.CommonModelField;
import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;

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
}