package cn.com.connext.msf.framework.dynamic;

import cn.com.connext.msf.framework.dynamic.builder.CommonModelBuilder;
import cn.com.connext.msf.framework.dynamic.builder.DataModelField;
import cn.com.connext.msf.framework.dynamic.entity.ApplicationLog;
import cn.com.connext.msf.framework.dynamic.entity.CdpDataModel;
import cn.com.connext.msf.framework.dynamic.entity.DataPersistentModel;
import cn.com.connext.msf.framework.dynamic.entity.OrderSales;
import cn.com.connext.msf.framework.utils.JSON;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

public class CommonModelBuilderTest {

    private final Logger logger = LoggerFactory.getLogger(CommonModelBuilderTest.class);

    @Test
    public void test01() {
        CommonModel<CommonModelField> orderSalesModel = CommonModelBuilder.build(OrderSales.class);
        logger.info("OrderSales Model: \n{}", JSON.toIndentJsonString(orderSalesModel));

        CommonModelField id = orderSalesModel.getFields().get(0);
        Assert.assertEquals("id", id.getName());
        Assert.assertEquals(DynamicModelFieldType.KEYWORD, id.getType());

        CommonModelField extend = orderSalesModel.getFields().get(6);
        Assert.assertEquals("extend", extend.getName());
        Assert.assertEquals(DynamicModelFieldType.NESTED, extend.getType());

        CommonModelField createdTime = orderSalesModel.getFields().get(7);
        Assert.assertEquals("createdTime", createdTime.getName());
        Assert.assertEquals(DynamicModelFieldType.DATE, createdTime.getType());
    }

    @Test
    public void test02() {
        CommonModel<CommonModelField> applicationLogModel = CommonModelBuilder.build(ApplicationLog.class);
        logger.info("ApplicationLog Model: \n{}", JSON.toIndentJsonString(applicationLogModel));

        CommonModelField appName = applicationLogModel.getFields().get(0);
        Assert.assertEquals("app_name", appName.getName());
        Assert.assertEquals(DynamicModelFieldType.KEYWORD, appName.getType());
    }

    @Test
    public void test03() {
        CommonModelField modelField = new CommonModelField();
        modelField.setName("字段名称");
        CommonModel model = CommonModelBuilder.build(new CommonModelBuilder.Builder()
                .setAliasName("显示名称")
                .setName("模型名称").addModelField(modelField));

        System.out.println(model);
    }

    @Test
    public void test04() {
        CdpDataModel cdpDataModel = buildDataModel();
        System.out.println(JSON.toJsonString(cdpDataModel));
        Assert.assertEquals(true, cdpDataModel instanceof DataPersistentModel);
        Assert.assertEquals(4, cdpDataModel.getFields().size());
        Assert.assertEquals(true, cdpDataModel.getFields().get(1) instanceof DataModelField);
        Assert.assertEquals(1, ((DataModelField) cdpDataModel.getFields().get(1)).getFields().size());
        Assert.assertEquals("字典id2", ((DataModelField) cdpDataModel.getFields().get(3)).getDictionaryId());
    }

    private CdpDataModel buildDataModel() {
        DataPersistentModel member = new DataPersistentModel();
        member.setTenantId("001");
        member.setCollectionId("EL");
        Supplier<DataModelField> supplier = () -> new DataModelField();

        List<DataModelField> fieldList = Lists.newArrayList(
//                (DataModelField) CommonModelFieldBuilder.from("来源编码", "brand", DynamicModelFieldType.KEYWORD).build(supplier),
//                (DataModelField) CommonModelFieldBuilder.from("会员编码", "cid", DynamicModelFieldType.KEYWORD).build(supplier),
//                (DataModelField) CommonModelFieldBuilder.from("真实姓名", "customerID", DynamicModelFieldType.KEYWORD).build(supplier),
//                (DataModelField) CommonModelFieldBuilder.from("手机号码", "customerSID", DynamicModelFieldType.KEYWORD).build(supplier),
                CommonModelField.from(supplier, "资料更新时间", "customerName", DynamicModelFieldType.KEYWORD)
        );
        List<DataModelField> inner = Lists.newArrayList(
                CommonModelField.from(supplier, "会员编码", "cid", DynamicModelFieldType.KEYWORD)
        );
        DataModelField field = CommonModelField.from(supplier, "真实姓名", "customerID", DynamicModelFieldType.KEYWORD);

        member.setFields(fieldList);
        member.addField(supplier, "来源编码", "brand", DynamicModelFieldType.NESTED, false, "", true, inner, null);
        member.addField(supplier, "手机号码", "customerSID", (x) -> x.setDictionaryId("字典id"));
        member.addField(field, (x) -> x.setDictionaryId("字典id2"));
        return member;
    }
}