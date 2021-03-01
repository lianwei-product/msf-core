package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.aviator.functions.AviatorTransfer;
import cn.com.connext.msf.framework.dynamic.CommonModel;
import cn.com.connext.msf.framework.dynamic.DynamicModel;
import cn.com.connext.msf.framework.dynamic.DynamicModelField;
import cn.com.connext.msf.framework.dynamic.builder.CommonModelBuilder;
import cn.com.connext.msf.framework.mapping.builder.CommonMappingModelBuilder;
import cn.com.connext.msf.framework.mapping.models.Member01;
import cn.com.connext.msf.framework.mapping.models.Member02;
import cn.com.connext.msf.framework.mapping.models.Member03;
import cn.com.connext.msf.framework.mapping.models.Member04;
import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.framework.utils.ObjectNodeUtil;
import cn.com.connext.msf.framework.utils.Time;
import cn.com.connext.msf.framework.utils.Validator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class DataMapperTest {

    @Test
    public void testParse() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildTestObjectNode();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        CommonModel commonModel = CommonModelBuilder.build(Member01.class);
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.build01();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("jd001", destNode.get("id").asText(null));
    }

    @Test
    public void testJD() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildJDMember(1);
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        CommonMappingModel commonModel = CommonMappingModelBuilder.build(Member02.class);
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildJD();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("ECommerce-JD", destNode.get("sourceCode").get(0).asText(null));
    }

    @Test
    public void testDefaultValue() {
        ObjectNode sourceNode = JsonNodeFactory.instance.objectNode();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        CommonMappingModel commonModel = CommonMappingModelBuilder.build(Member03.class);
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildDefault();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("男", destNode.get("gender").asText(null));
    }

    @Test
    public void testNestedDefaultValue01() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedDefaultValue01();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNestedDefaultValue();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedDefaultValue();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("ChengHan", destNode.get("name").get("full").asText(null));
    }

    @Test
    public void testNestedDefaultValue02() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedDefaultValue02();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNestedDefaultValue();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedDefaultValue();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("UNKNOWN", destNode.get("name").get("full").asText(null));
    }

    @Test
    public void testNestedArrayDefaultValue() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedArrayDefaultValue01();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNestedArrayDefaultValue();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedArrayDefaultValue();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("ChengHan", destNode.get("name").get("full").get(0).asText(null));


        ObjectNode destNode2 = DataMapper.mapping(SampleObjectNodeDataBuilder.buildNestedArrayDefaultValue02(), commonModel, mappings);
        System.out.println("------------destNode2---------------");
        System.out.println(JSON.toIndentJsonString(destNode2));

        Assert.assertEquals("UNKNOWN", destNode2.get("name").get("full").get(0).asText(null));
    }

    @Test
    public void testFixedValue() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildFixedValue();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildFixedValue();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildFixedValue();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("120.0", destNode.get("amount").asText(null));
    }

    @Test
    public void testDictConvert() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildDictConvert(999);
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildDictConvert();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildDictConvert();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals(null, destNode.get("gender"));

        ObjectNode sourceNode2 = SampleObjectNodeDataBuilder.buildDictConvert(0);
        System.out.println("------------sourceNode2---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode2));

        ObjectNode destNode2 = DataMapper.mapping(sourceNode2, commonModel, mappings);
        System.out.println("------------destNode2---------------");
        System.out.println(JSON.toIndentJsonString(destNode2));

        Assert.assertEquals(0, destNode2.get("gender").intValue());

        ObjectNode sourceNode3 = SampleObjectNodeDataBuilder.buildDictConvert(1);
        System.out.println("------------sourceNode3---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode3));

        ObjectNode destNode3 = DataMapper.mapping(sourceNode3, commonModel, mappings);
        System.out.println("------------destNode3---------------");
        System.out.println(JSON.toIndentJsonString(destNode3));

        Assert.assertEquals(1, destNode3.get("gender").intValue());

    }

    @Test
    public void jsonNodeTypeTest() {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("01", "abc");
        objectNode.put("02", Time.getTimeFormatString(LocalDateTime.now()));
        objectNode.put("03", 10);
        objectNode.put("04", 12345678);
        objectNode.put("05", 12345678912345L);
        objectNode.put("06", 125.5);
        objectNode.put("07", true);

        objectNode.fieldNames().forEachRemaining(field -> {
            System.out.println(field = "field:" + field + ", type:" + objectNode.get(field).getNodeType().name() + ", value: " + objectNode.get(field).asText(null));
        });


        Assert.assertEquals(true, objectNode.get("01").isTextual());
        Assert.assertEquals(false, objectNode.get("01").isNumber());
        Assert.assertEquals(true, objectNode.get("02").isTextual());
        Assert.assertEquals(false, objectNode.get("02").isNumber());

        Assert.assertEquals(true, objectNode.get("03").isNumber() && Validator.isShort(objectNode.get("03").asText(null)));
        Assert.assertEquals(true, objectNode.get("04").isNumber() && Validator.isInteger(objectNode.get("04").asText(null)));
        Assert.assertEquals(true, objectNode.get("05").isNumber() && Validator.isLong(objectNode.get("05").asText(null)));
        Assert.assertEquals(true, objectNode.get("06").isNumber() && Validator.isDouble(objectNode.get("06").asText(null)));
    }

    @Test
    public void testMultiSourceFieldConvert01() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildMultiSourceFieldConvert01();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        CommonMappingModel commonModel = CommonMappingModelBuilder.build(Member04.class);
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildMultiSourceFieldConvert01("-");
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("江苏省-南京市-玄武区", destNode.get("address").textValue());
    }

    @Test
    public void testMultiSourceFieldConvert02() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildMultiSourceFieldConvert01();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        CommonMappingModel commonModel = CommonMappingModelBuilder.build(Member04.class);
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildMultiSourceFieldConvert01("，");
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("江苏省，南京市，玄武区", destNode.get("address").textValue());
    }

    @Test
    public void testConditionConvert01() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildConditionConvert01();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildConditionConvert01();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildConditionConvert01();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("未知", destNode.get("level").textValue());

    }

    @Test
    public void testConditionConvert02() {
        ObjectNode sourceNode1 = SampleObjectNodeDataBuilder.buildConditionConvert02("20");
        System.out.println("------------sourceNode1---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode1));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildConditionConvert01();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildConditionConvert01();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode1 = DataMapper.mapping(sourceNode1, commonModel, mappings);
        System.out.println("------------destNode1---------------");
        System.out.println(JSON.toIndentJsonString(destNode1));
        Assert.assertEquals("A", destNode1.get("level").textValue());


        ObjectNode sourceNode2 = SampleObjectNodeDataBuilder.buildConditionConvert02("60");
        System.out.println("------------sourceNode2---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode2));

        ObjectNode destNode2 = DataMapper.mapping(sourceNode2, commonModel, mappings);
        System.out.println("------------destNode2---------------");
        System.out.println(JSON.toIndentJsonString(destNode2));
        Assert.assertEquals("B", destNode2.get("level").textValue());

        ObjectNode sourceNode3 = SampleObjectNodeDataBuilder.buildConditionConvert02("160");
        System.out.println("------------sourceNode3---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode3));

        ObjectNode destNode3 = DataMapper.mapping(sourceNode3, commonModel, mappings);
        System.out.println("------------destNode3---------------");
        System.out.println(JSON.toIndentJsonString(destNode3));
        Assert.assertEquals("C", destNode3.get("level").textValue());

        ObjectNode sourceNode4 = SampleObjectNodeDataBuilder.buildConditionConvert02("160L");
        System.out.println("------------sourceNode4---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode4));

        ObjectNode destNode4 = DataMapper.mapping(sourceNode4, commonModel, mappings);
        System.out.println("------------destNode4---------------");
        System.out.println(JSON.toIndentJsonString(destNode4));
        Assert.assertEquals("未知", destNode4.get("level").textValue());

    }

    @Test
    public void testConditionConvert03() {
        ObjectNode sourceNode1 = SampleObjectNodeDataBuilder.buildConditionConvert03("20");
        System.out.println("------------sourceNode1---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode1));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildConditionConvert02();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildConditionConvert02();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode1 = DataMapper.mapping(sourceNode1, commonModel, mappings);
        System.out.println("------------destNode1---------------");
        System.out.println(JSON.toIndentJsonString(destNode1));
        Assert.assertEquals("A", ObjectNodeUtil.getString(destNode1, "name.level"));


        ObjectNode sourceNode2 = SampleObjectNodeDataBuilder.buildConditionConvert03("60");
        System.out.println("------------sourceNode2---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode2));

        ObjectNode destNode2 = DataMapper.mapping(sourceNode2, commonModel, mappings);
        System.out.println("------------destNode2---------------");
        System.out.println(JSON.toIndentJsonString(destNode2));
        Assert.assertEquals("B", ObjectNodeUtil.getString(destNode2, "name.level"));

        ObjectNode sourceNode3 = SampleObjectNodeDataBuilder.buildConditionConvert03("160");
        System.out.println("------------sourceNode3---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode3));

        ObjectNode destNode3 = DataMapper.mapping(sourceNode3, commonModel, mappings);
        System.out.println("------------destNode3---------------");
        System.out.println(JSON.toIndentJsonString(destNode3));
        Assert.assertEquals("C", ObjectNodeUtil.getString(destNode3, "name.level"));

        ObjectNode sourceNode4 = SampleObjectNodeDataBuilder.buildConditionConvert03("160L");
        System.out.println("------------sourceNode4---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode4));

        ObjectNode destNode4 = DataMapper.mapping(sourceNode4, commonModel, mappings);
        System.out.println("------------destNode4---------------");
        System.out.println(JSON.toIndentJsonString(destNode4));
        Assert.assertEquals("未知", ObjectNodeUtil.getString(destNode4, "name.level"));

    }

    @Test
    public void testConditionConvert04() {
        ObjectNode sourceNode1 = SampleObjectNodeDataBuilder.buildConditionConvert03("20");
        System.out.println("------------sourceNode1---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode1));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildConditionConvert02();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildConditionConvert03();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        AviatorEvaluator.addFunction(new SampleCommonModelMappingBuilder.LevelTransfer());

        ObjectNode destNode1 = DataMapper.mapping(sourceNode1, commonModel, mappings);
        System.out.println("------------destNode1---------------");
        System.out.println(JSON.toIndentJsonString(destNode1));
        Assert.assertEquals("A", ObjectNodeUtil.getString(destNode1, "name.level"));

    }


    @Test
    public void testConditionConvert05() {
        ObjectNode sourceNode1 = SampleObjectNodeDataBuilder.buildConditionConvert04("20", "30");
        System.out.println("------------sourceNode1---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode1));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildConditionConvert03();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildConditionConvert04();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        try {
            AviatorEvaluator.importFunctions(AviatorTransfer.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        ObjectNode destNode1 = DataMapper.mapping(sourceNode1, commonModel, mappings);
        System.out.println("------------destNode1---------------");
        System.out.println(JSON.toIndentJsonString(destNode1));
        Assert.assertEquals("50.0", ObjectNodeUtil.getString(destNode1, "name.score"));

    }

    @Test
    public void testDynamicAndCommonConvert() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildDataModel();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        CommonMappingModel commonMappingModel = SampleDynamicModelBuilder.buildDataModel();
        System.out.println("------------commonMappingModel---------------");
        System.out.println(JSON.toIndentJsonString(commonMappingModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildDataModel();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonMappingModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("0001", destNode.get("sourceIdsDest").get("cidDest").asText(null));
    }

    @Test
    public void testNullValue() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedDefaultValue02();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNullValue();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedDefaultValue();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertNull(destNode.get("name").get("full"));
    }

    @Test
    public void testNestMapping01() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedMapping01();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNestedMapping01();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));
//
        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedMapping01();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));
//
        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));
//
        Assert.assertEquals("江蘇", destNode.get("province").asText());
        Assert.assertEquals("南京", destNode.get("city").asText());
    }

    @Test
    public void testNestMapping02() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedMapping01();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNestedMapping02();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));
//
        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedMapping02();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals("江蘇", destNode.get("addressInfo").get("province").asText());
        Assert.assertEquals("南京", destNode.get("addressInfo").get("city").asText());
    }

    @Test
    public void testNestMapping03() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedMapping01();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNestedMapping03();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));
//
        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedMapping03();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));
//

        Assert.assertEquals("江蘇", destNode.get("addressInfo").get(0).get("province").asText());
        Assert.assertEquals("南京", destNode.get("addressInfo").get(0).get("city").asText());
        Assert.assertEquals("安徽", destNode.get("addressInfo").get(1).get("province").asText());
        Assert.assertEquals("蕪湖", destNode.get("addressInfo").get(1).get("city").asText());
    }

    @Test
    public void testNestMapping04() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedMapping04();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNestedMapping03();
        System.out.println("------------commonModel---------------");
        System.out.println(JSON.toIndentJsonString(commonModel));
//
        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedMapping04();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

    }

    @Test
    public void testStr2Array() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildStr2Array();
        System.out.println("------------sourceNode---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode));

        DynamicModel<DynamicModelField> commonMappingModel = SampleDynamicModelBuilder.buildStr2Array();
        System.out.println("------------commonMappingModel---------------");
        System.out.println(JSON.toIndentJsonString(commonMappingModel));

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildStr2Array();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        ObjectNode destNode = DataMapper.mapping(sourceNode, commonMappingModel, mappings);
        System.out.println("------------destNode---------------");
        System.out.println(JSON.toIndentJsonString(destNode));

        Assert.assertEquals(1, destNode.get("addressInfo").size());
    }

    @Test
    public void testSimpleMappingCondition() {
        ObjectNode sourceNode1 = SampleObjectNodeDataBuilder.buildConditionConvert03("20");
        System.out.println("------------sourceNode1---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode1));


        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildSimpleConditionConvert01();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        Object destNode1 = DataMapper.simpleMapping(sourceNode1, String.class, mappings.get(0), false);
        System.out.println("------------destNode1---------------");
        System.out.println(JSON.toIndentJsonString(destNode1));

    }

    @Test
    public void testSimpleMappingNumber() {
        testSimpleMappingNumberItem(Integer.class);
        testSimpleMappingNumberItem(Long.class);
        testSimpleMappingNumberItem(Double.class);
        testSimpleMappingNumberItem(Float.class);

    }

    public void testSimpleMappingNumberItem(Class clazz) {
        ObjectNode sourceNode1 = SampleObjectNodeDataBuilder.buildTestObjectNode();
        System.out.println("------------sourceNode1---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode1));


        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildSimpleDefault();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        Object destNode1 = DataMapper.simpleMapping(sourceNode1, clazz, mappings.get(0), false);
        System.out.println("------------destNode1---------------");
        System.out.println(JSON.toIndentJsonString(destNode1));
        System.out.println("================================================");

    }

    @Test
    public void testSimpleMappingBool() {
        ObjectNode sourceNode1 = SampleObjectNodeDataBuilder.buildTestObjectNode();
        System.out.println("------------sourceNode1---------------");
        System.out.println(JSON.toIndentJsonString(sourceNode1));


        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildSimpleDefault02();
        System.out.println("------------mappings---------------");
        System.out.println(JSON.toIndentJsonString(mappings));

        Object destNode1 = DataMapper.simpleMapping(sourceNode1, Boolean.class, mappings.get(0), false);
        System.out.println("------------destNode1---------------");
        System.out.println(JSON.toIndentJsonString(destNode1));

    }

}