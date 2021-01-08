package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.dynamic.DynamicModel;
import cn.com.connext.msf.framework.dynamic.DynamicModelField;
import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import cn.com.connext.msf.framework.mapping.builder.CommonMappingModelBuilder;
import cn.com.connext.msf.framework.mapping.entity.CdpDataModelField;
import cn.com.connext.msf.framework.mapping.entity.CdpDataPersistentModel;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Supplier;

public class SampleDynamicModelBuilder {
    static Supplier<CdpDataModelField> fieldSupplier = () -> new CdpDataModelField();


    public static DynamicModel<DynamicModelField> buildNestedDefaultValue() {
        CdpDataModelField field01 = CdpDataModelField.from(fieldSupplier, "full", "full", DynamicModelFieldType.KEYWORD, false, "UNKNOWN");

        CdpDataModelField field02 = CdpDataModelField.from(fieldSupplier, "first", "first");

        CdpDataModelField field03 = CdpDataModelField.from(fieldSupplier, "last", "last");

        CdpDataModelField field = CdpDataModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);
        field.getFields().add(field02);
        field.getFields().add(field03);

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(field));
    }

    public static DynamicModel<DynamicModelField> buildNullValue() {
        CdpDataModelField field01 = CdpDataModelField.from(fieldSupplier, "full", "full");

        CdpDataModelField field02 = CdpDataModelField.from(fieldSupplier, "first", "first");

        CdpDataModelField field03 = CdpDataModelField.from(fieldSupplier, "last", "last");

        CdpDataModelField field = CdpDataModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);
        field.getFields().add(field02);
        field.getFields().add(field03);

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(field));
    }

    public static DynamicModel<DynamicModelField> buildNestedArrayDefaultValue() {
        CdpDataModelField field01 = CdpDataModelField.from(fieldSupplier, "full", "full", DynamicModelFieldType.KEYWORD, true, "UNKNOWN");

        CdpDataModelField field = CdpDataModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(field));
    }

    public static DynamicModel<DynamicModelField> buildFixedValue() {
        CdpDataModelField field = CdpDataModelField.from(fieldSupplier, "amount", "amount", DynamicModelFieldType.DOUBLE);

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(field));
    }

    public static DynamicModel<DynamicModelField> buildDictConvert() {
        CdpDataModelField field = CdpDataModelField.from(fieldSupplier, "gender", "gender", DynamicModelFieldType.INTEGER);

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(field));
    }

    public static DynamicModel<DynamicModelField> buildConditionConvert01() {
        CdpDataModelField field = CdpDataModelField.from(fieldSupplier, "level", "level", DynamicModelFieldType.KEYWORD, false, "unregisted");

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(field));
    }

    public static DynamicModel<DynamicModelField> buildConditionConvert02() {
        CdpDataModelField field01 = CdpDataModelField.from(fieldSupplier, "level", "level", DynamicModelFieldType.KEYWORD, false, "UNKNOWN");

        CdpDataModelField field = CdpDataModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(field));
    }

    public static DynamicModel<DynamicModelField> buildConditionConvert03() {
        CdpDataModelField field01 = CdpDataModelField.from(fieldSupplier, "score", "score", DynamicModelFieldType.KEYWORD, false, "UNKNOWN");

        CdpDataModelField field = CdpDataModelField.from(fieldSupplier, "name", "name", DynamicModelFieldType.NESTED);
        field.getFields().add(field01);

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(field));
    }


    public static CommonMappingModel buildDataModel() {
        CdpDataPersistentModel member = new CdpDataPersistentModel();
        member.setTenantId("001");
        member.setCollectionId("EL");
        Supplier<CdpDataModelField> supplier = () -> new CdpDataModelField();

        List<CdpDataModelField> fieldList = Lists.newArrayList(
                CdpDataModelField.from(supplier, "customerNameDest", "会员名称", DynamicModelFieldType.KEYWORD)
        );
        List<CdpDataModelField> inner = Lists.newArrayList(
                CdpDataModelField.from(supplier, "cidDest", "会员编码", DynamicModelFieldType.KEYWORD)
        );
        CdpDataModelField field = CdpDataModelField.from(supplier, "realNameDest", "真实姓名", DynamicModelFieldType.KEYWORD);

        member.setFields(fieldList);
        member.addField(supplier, "sourceIdsDest", "来源编码", DynamicModelFieldType.NESTED, false, "", true, inner, null);
        member.addField(supplier, "phoneDest", "手机号码", (x) -> x.setDictionaryId("字典id"));
        member.addField(field, (x) -> x.setDictionaryId("字典id2"));
        return member;
    }

    public static DynamicModel<DynamicModelField> buildNestedMapping01() {
        CdpDataModelField province = CdpDataModelField.from(fieldSupplier, "province", "province");
        CdpDataModelField city = CdpDataModelField.from(fieldSupplier, "city", "city");

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(province)
                .addModelField(city)
        );
    }

    public static DynamicModel<DynamicModelField> buildNestedMapping02() {
        CdpDataModelField addressInfo = CdpDataModelField.from(fieldSupplier, "addressInfo", "addressInfo", DynamicModelFieldType.NESTED, null);
        CdpDataModelField province = CdpDataModelField.from(fieldSupplier, "province", "province");
        CdpDataModelField city = CdpDataModelField.from(fieldSupplier, "city", "city");
        addressInfo.setFields(Lists.newArrayList(province, city));

        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(addressInfo)
        );
    }

    public static DynamicModel<DynamicModelField> buildNestedMapping03() {
        CdpDataModelField addressInfo = CdpDataModelField.from(fieldSupplier, "addressInfo", "addressInfo", DynamicModelFieldType.NESTED, null);
        CdpDataModelField province = CdpDataModelField.from(fieldSupplier, "province", "province");
        CdpDataModelField city = CdpDataModelField.from(fieldSupplier, "city", "city");
        addressInfo.setFields(Lists.newArrayList(province, city));
        addressInfo.setArrayType(true);
        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(addressInfo)
        );
    }

    public static DynamicModel<DynamicModelField> buildStr2Array() {
        CdpDataModelField addressInfo = CdpDataModelField.from(fieldSupplier, "addressInfo", "addressInfo", DynamicModelFieldType.NESTED, null);
        CdpDataModelField province = CdpDataModelField.from(fieldSupplier, "province", "province");
        addressInfo.setFields(Lists.newArrayList(province));
        addressInfo.setArrayType(true);
        return CommonMappingModelBuilder.build(new CommonMappingModelBuilder.Builder()
                .addModelField(addressInfo)
        );

    }


}
