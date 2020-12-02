package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.dynamic.CommonModel;
import cn.com.connext.msf.framework.dynamic.CommonModelField;
import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import cn.com.connext.msf.framework.dynamic.builder.CommonModelBuilder;
import cn.com.connext.msf.framework.dynamic.builder.DataModelField;
import cn.com.connext.msf.framework.dynamic.entity.CdpDataModel;
import cn.com.connext.msf.framework.dynamic.entity.DataPersistentModel;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Supplier;

public class SampleCommonModelBuilder {
    static Supplier<CommonModelField> fieldSupplier = () -> new CommonModelField();


    public static CommonModel buildNestedDefaultValue() {
        CommonModelField field01 = CommonModelField.from(fieldSupplier, "full", "full", DynamicModelFieldType.KEYWORD, false, "UNKNOWN");

        CommonModelField field02 = CommonModelField.from(fieldSupplier, "first", "first");

        CommonModelField field03 = CommonModelField.from(fieldSupplier, "last", "last");

        CommonModelField field = CommonModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);
        field.getFields().add(field02);
        field.getFields().add(field03);

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(field));
    }

    public static CommonModel buildNullValue() {
        CommonModelField field01 = CommonModelField.from(fieldSupplier, "full", "full");

        CommonModelField field02 = CommonModelField.from(fieldSupplier, "first", "first");

        CommonModelField field03 = CommonModelField.from(fieldSupplier, "last", "last");

        CommonModelField field = CommonModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);
        field.getFields().add(field02);
        field.getFields().add(field03);

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(field));
    }

    public static CommonModel buildNestedArrayDefaultValue() {
        CommonModelField field01 = CommonModelField.from(fieldSupplier, "full", "full", DynamicModelFieldType.KEYWORD, true, "UNKNOWN");

        CommonModelField field = CommonModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(field));
    }

    public static CommonModel buildFixedValue() {
        CommonModelField field = CommonModelField.from(fieldSupplier, "amount", "amount", DynamicModelFieldType.DOUBLE);

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(field));
    }

    public static CommonModel buildDictConvert() {
        CommonModelField field = CommonModelField.from(fieldSupplier, "gender", "gender", DynamicModelFieldType.INTEGER);

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(field));
    }

    public static CommonModel buildConditionConvert01() {
        CommonModelField field = CommonModelField.from(fieldSupplier, "level", "level", DynamicModelFieldType.KEYWORD, false, "unregisted");

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(field));
    }

    public static CommonModel buildConditionConvert02() {
        CommonModelField field01 = CommonModelField.from(fieldSupplier, "level", "level", DynamicModelFieldType.KEYWORD, false, "UNKNOWN");

        CommonModelField field = CommonModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(field));
    }


    public static CdpDataModel buildDataModel() {
        DataPersistentModel member = new DataPersistentModel();
        member.setTenantId("001");
        member.setCollectionId("EL");
        Supplier<DataModelField> supplier = () -> new DataModelField();

        List<DataModelField> fieldList = Lists.newArrayList(
                CommonModelField.from(supplier, "customerNameDest", "会员名称", DynamicModelFieldType.KEYWORD)
        );
        List<DataModelField> inner = Lists.newArrayList(
                CommonModelField.from(supplier, "cidDest", "会员编码", DynamicModelFieldType.KEYWORD)
        );
        DataModelField field = CommonModelField.from(supplier, "realNameDest", "真实姓名", DynamicModelFieldType.KEYWORD);

        member.setFields(fieldList);
        member.addField(supplier, "sourceIdsDest", "来源编码", DynamicModelFieldType.NESTED, false, "", true, inner, null);
        member.addField(supplier, "phoneDest", "手机号码", (x) -> x.setDictionaryId("字典id"));
        member.addField(field, (x) -> x.setDictionaryId("字典id2"));
        return member;
    }

    public static CommonModel buildNestedMapping01() {
        CommonModelField province = CommonModelField.from(fieldSupplier, "province", "province");
        CommonModelField city = CommonModelField.from(fieldSupplier, "city", "city");

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(province)
                .addModelField(city)
        );
    }

    public static CommonModel buildNestedMapping02() {
        CommonModelField addressInfo = CommonModelField.from(fieldSupplier, "addressInfo", "addressInfo", DynamicModelFieldType.NESTED, null);
        CommonModelField province = CommonModelField.from(fieldSupplier, "province", "province");
        CommonModelField city = CommonModelField.from(fieldSupplier, "city", "city");
        addressInfo.setFields(Lists.newArrayList(province, city));

        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(addressInfo)
        );
    }

    public static CommonModel buildNestedMapping03() {
        CommonModelField addressInfo = CommonModelField.from(fieldSupplier, "addressInfo", "addressInfo", DynamicModelFieldType.NESTED, null);
        CommonModelField province = CommonModelField.from(fieldSupplier, "province", "province");
        CommonModelField city = CommonModelField.from(fieldSupplier, "city", "city");
        addressInfo.setFields(Lists.newArrayList(province, city));
        addressInfo.setArrayType(true);
        return CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .addModelField(addressInfo)
        );
    }
}
