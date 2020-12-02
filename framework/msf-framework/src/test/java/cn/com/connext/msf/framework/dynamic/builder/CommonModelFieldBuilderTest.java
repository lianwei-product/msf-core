package cn.com.connext.msf.framework.dynamic.builder;

import cn.com.connext.msf.framework.dynamic.CommonModelField;
import cn.com.connext.msf.framework.dynamic.DynamicModelFieldType;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Supplier;

public class CommonModelFieldBuilderTest {
    Supplier<CommonModelField> fieldSupplier = () -> new CommonModelField();

    public CommonModelFieldBuilderTest() {
    }

    @Test
    public void testBuild01() {
        CommonModelField<CommonModelField> field = CommonModelField.from(fieldSupplier, "field1-1", "字段1-1", DynamicModelFieldType.KEYWORD, false, "男");
        CommonModelField<CommonModelField> field01 = CommonModelField.from(fieldSupplier, "field1", "字段1", DynamicModelFieldType.NESTED, false);
        field01.getFields().add(field);

        System.out.println(field01);

        Assert.assertEquals("男", field01.getFields().get(0).getDefaultValue());
    }

    @Test
    public void testBuild02() {
        CommonModelField<CommonModelField> field = CommonModelField.from(fieldSupplier, "field1-1", "字段1-1", DynamicModelFieldType.KEYWORD, false, "男");
        CommonModelField<CommonModelField> field01 = CommonModelField.from(fieldSupplier, "field1", "字段1", DynamicModelFieldType.NESTED, false, null, false, Lists.newArrayList(field), null);

        System.out.println(field01);

        Assert.assertEquals("男", field01.getFields().get(0).getDefaultValue());
    }

    @Test
    public void testBuild03() {
        Supplier<DataModelField> supplier = () -> new DataModelField();
        DataModelField field = CommonModelField.from(supplier, "field1-1", "字段1-1", DynamicModelFieldType.KEYWORD, false, "男");
        DataModelField field01 = CommonModelField.from(supplier, "field1", "字段1", DynamicModelFieldType.NESTED, false, null, false, Lists.newArrayList(field), null);
        field01.setDictionaryId("字典ID");

        System.out.println(field01);

        Assert.assertEquals("男", field01.getFields().get(0).getDefaultValue());
    }

    @Test
    public void testBuild04() {
        Supplier<DataModelField> supplier = () -> new DataModelField();
        DataModelField field = CommonModelField.from(supplier, "field1-1", "字段1-1", DynamicModelFieldType.KEYWORD, false, "男");
        DataModelField field01 = CommonModelField.from(supplier, "field1", "字段1", DynamicModelFieldType.NESTED, false, null, false, Lists.newArrayList(field), (x) -> x.setDictionaryId("字典ID"));

        System.out.println(field01);

        Assert.assertEquals("男", field01.getFields().get(0).getDefaultValue());
    }
}