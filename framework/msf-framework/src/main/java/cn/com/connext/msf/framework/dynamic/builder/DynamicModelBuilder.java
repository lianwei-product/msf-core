package cn.com.connext.msf.framework.dynamic.builder;

import cn.com.connext.msf.framework.dynamic.DynamicModel;
import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("WeakerAccess")
public abstract class DynamicModelBuilder {

    protected final static Map<Class, DynamicModelFieldType> STANDARD_TYPE = new HashMap<Class, DynamicModelFieldType>() {
        {
            put(short.class, DynamicModelFieldType.SHORT);
            put(int.class, DynamicModelFieldType.INTEGER);
            put(long.class, DynamicModelFieldType.LONG);
            put(float.class, DynamicModelFieldType.FLOAT);
            put(double.class, DynamicModelFieldType.DOUBLE);
            put(boolean.class, DynamicModelFieldType.BOOLEAN);

            put(Short.class, DynamicModelFieldType.SHORT);
            put(Integer.class, DynamicModelFieldType.INTEGER);
            put(Long.class, DynamicModelFieldType.LONG);
            put(Float.class, DynamicModelFieldType.FLOAT);
            put(Double.class, DynamicModelFieldType.DOUBLE);
            put(Boolean.class, DynamicModelFieldType.BOOLEAN);

            put(String.class, DynamicModelFieldType.KEYWORD);
            put(Date.class, DynamicModelFieldType.DATE);
            put(BigDecimal.class, DynamicModelFieldType.DOUBLE);
        }
    };

    public abstract DynamicModel build();


    protected String getName(Field field) {
        JsonProperty annotation = field.getDeclaredAnnotation(JsonProperty.class);
        return annotation != null && !StringUtils.isBlank(annotation.value())
                ? annotation.value()
                : field.getName();
    }

    protected String getAliasName(Field field) {
        ApiModelProperty annotation = field.getDeclaredAnnotation(ApiModelProperty.class);
        return annotation != null && !StringUtils.isBlank(annotation.value())
                ? annotation.value()
                : field.getName();
    }

    protected boolean isArray(Field field) {
        return field.getType() == List.class;
    }

    protected DynamicModelFieldType getModelFieldType(Class<?> fieldClass) {
        DynamicModelFieldType modelFieldType = STANDARD_TYPE.get(fieldClass);
        return modelFieldType != null ? modelFieldType : DynamicModelFieldType.NESTED;
    }

    protected Class<?> getFieldClass(Field field) {
        if (field.getType() == List.class) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                return (Class<?>) pt.getActualTypeArguments()[0];
            }
        }
        return field.getType();
    }

}
