package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.dynamic.CommonModel;
import cn.com.connext.msf.framework.dynamic.DynamicModel;
import cn.com.connext.msf.framework.dynamic.DynamicModelField;
import cn.com.connext.msf.framework.dynamic.builder.CommonModelBuilder;
import cn.com.connext.msf.framework.mapping.models.Member01;
import cn.com.connext.msf.framework.mapping.models.Member02;
import cn.com.connext.msf.framework.mapping.models.Member03;
import cn.com.connext.msf.framework.utils.StreamUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

import java.util.List;

// TODO mapper 性能单元测试
public class DataMapperPerformanceTest {
    long times = 1000000;
    @Rule
    public MethodRule watchman = new TestWatchman() {
        public void starting(FrameworkMethod method) {
            System.out.println("Starting test: " + method.getName());
        }
    };

    @Test
    public void testParse() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildTestObjectNode();

        CommonModel commonModel = CommonModelBuilder.build(Member01.class);

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.build01();

//        long times = 10000;
        long start = System.currentTimeMillis();
        StreamUtil.consumer(times,null, (x) -> {
           DataMapper.mapping(sourceNode, commonModel, mappings);
        });
        long end = System.currentTimeMillis();
        System.out.println("执行" + times + "次，耗时:" + (end - start) + "ms\n");
    }

    @Test
    public void testJD() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildJDMember(1);

        CommonModel commonModel = CommonModelBuilder.build(Member02.class);

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildJD();

//        long times = 10000;
        long start = System.currentTimeMillis();
        StreamUtil.consumer(times,null,  (x) -> {
            DataMapper.mapping(sourceNode, commonModel, mappings);
        });
        long end = System.currentTimeMillis();
        System.out.println("执行" + times + "次，耗时:" + (end - start) + "ms\n");
    }

    @Test
    public void testDefaultValue() {
        ObjectNode sourceNode = JsonNodeFactory.instance.objectNode();

        CommonModel commonModel = CommonModelBuilder.build(Member03.class);

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildDefault();

//        long times = 10000;
        long start = System.currentTimeMillis();
        StreamUtil.consumer(times,null,  (x) -> {
            DataMapper.mapping(sourceNode, commonModel, mappings);
        });
        long end = System.currentTimeMillis();
        System.out.println("执行" + times + "次，耗时:" + (end - start) + "ms\n");
    }

    @Test
    public void testNestedDefaultValue01() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedDefaultValue01();

        DynamicModel<DynamicModelField> commonModel = SampleDynamicModelBuilder.buildNestedDefaultValue();

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedDefaultValue();

//        long times = 10000;
        long start = System.currentTimeMillis();
        StreamUtil.consumer(times,null,  (x) -> {
            DataMapper.mapping(sourceNode, commonModel, mappings);
        });
        long end = System.currentTimeMillis();
        System.out.println("执行" + times + "次，耗时:" + (end - start) + "ms\n");

    }

    @Test
    public void testNestedDefaultValue02() {
        ObjectNode sourceNode = SampleObjectNodeDataBuilder.buildNestedDefaultValue02();

        DynamicModel<DynamicModelField>  commonModel = SampleDynamicModelBuilder.buildNestedDefaultValue();

        List<DynamicModelMapping> mappings = SampleCommonModelMappingBuilder.buildNestedDefaultValue();

//        long times = 10000;
        long start = System.currentTimeMillis();
        StreamUtil.consumer(times,null,  (x) -> {
            DataMapper.mapping(sourceNode, commonModel, mappings);
        });
        long end = System.currentTimeMillis();
        System.out.println("执行" + times + "次，耗时:" + (end - start) + "ms\n");
    }

}