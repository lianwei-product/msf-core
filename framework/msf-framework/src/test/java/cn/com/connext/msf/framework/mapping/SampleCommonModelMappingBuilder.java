package cn.com.connext.msf.framework.mapping;

import cn.com.connext.msf.framework.mapping.model.AviatorModel;
import cn.com.connext.msf.framework.mapping.model.DictModel;
import cn.com.connext.msf.framework.mapping.model.MultiSourceFieldModel;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.assertj.core.util.Lists;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SampleCommonModelMappingBuilder {
    static CommonModelMappingBuilder<CommonModelMapping> builder = new CommonModelMappingBuilder();

    static Supplier<CommonModelMapping> supplier = () -> new CommonModelMapping();

    public static List<DynamicModelMapping> build01() {
        return Lists.newArrayList(
                builder.buildFromSourceField(supplier, "id"),
                builder.buildFromSourceField(supplier, "realName"),
                builder.buildFromSourceField(supplier, "gender"),
                builder.buildFromSourceField(supplier, "mobile"),
                builder.buildFromSourceField(supplier, "telephone", "telephones"),
                builder.buildFromSourceField(supplier, "telephones", "telephones"),
                builder.buildFromSourceField(supplier, "birthDate"),
                builder.buildFromSourceField(supplier, "pro", "province"),
                builder.buildFromSourceField(supplier, "city"),
                builder.buildFromSourceField(supplier, "addressTypes", "addressInfo.type"),
                builder.buildFromSourceField(supplier, "addressList.type", "addressInfo.type"),
                builder.buildFromSourceField(supplier, "addressList.address", "addressInfo.address"),
                builder.buildFromSourceField(supplier, "lastAddress.type", "lastAddressInfo.type"),
                builder.buildFromSourceField(supplier, "lastAddress.address", "lastAddressInfo.address"),
                builder.buildFromSourceField(supplier, "behavior.type"),
                builder.buildFromSourceField(supplier, "behavior.detail.source"),
                builder.buildFromSourceField(supplier, "behavior.detail.action")
        );
    }

    public static List<DynamicModelMapping> buildJD() {
        return Lists.newArrayList(
                builder.buildFixedValue(supplier, "sourceCode", "ECommerce-JD"),
                builder.buildFromSourceField(supplier, "jdMemberId", "id"),
                builder.buildFromSourceField(supplier, "realName", "realName"),
                builder.buildFromSourceField(supplier, "gender"),
                builder.buildFromSourceField(supplier, "mobile"),
                builder.buildFromSourceField(supplier, "birthDate"),
                builder.buildFromSourceField(supplier, "province"),
                builder.buildFromSourceField(supplier, "city"),
                builder.buildFromSourceField(supplier, "addressList.type", "addressInfo.type"),
                builder.buildFromSourceField(supplier, "addressList.address", "addressInfo.address"),
                builder.buildFromSourceField(supplier, "updateTime", "updateTime")
        );
    }

    public static List<DynamicModelMapping> buildDefault() {
        return Lists.newArrayList(
                builder.buildFromSourceField(supplier, "gender")
        );
    }

    public static List<DynamicModelMapping> buildNestedDefaultValue() {
        return Lists.newArrayList(
                builder.buildFromSourceField(supplier, "name.full", "name.fullName"),
                builder.buildFromSourceField(supplier, "name.first", "name.firstName"),
                builder.buildFromSourceField(supplier, "name.last", "name.lastName")
        );
    }

    public static List<DynamicModelMapping> buildNestedArrayDefaultValue() {
        return Lists.newArrayList(
                builder.buildFromSourceField(supplier, "name.full", "name.fullName")
        );
    }

    public static List<DynamicModelMapping> buildFixedValue() {
        return Lists.newArrayList(
                builder.buildFixedValue(supplier, "amount", 120.0)
        );
    }

    public static List<DynamicModelMapping> buildDictConvert() {
        DictModel dictModel = new DictModel();
        dictModel.addDictItem("女", "0");
        dictModel.addDictItem("男", "1");

        return Lists.newArrayList(
                builder.buildFromDictionary(supplier, "gender", "gender_cn", dictModel)
        );
    }

    public static List<DynamicModelMapping> buildMultiSourceFieldConvert01(String separator) {
        MultiSourceFieldModel multiSourceFieldModel = new MultiSourceFieldModel();
        multiSourceFieldModel.setSeparator(separator);
        multiSourceFieldModel.setSourceFieldNames(Arrays.asList("province", "city", "district"));

        return Lists.newArrayList(
                builder.buildFromMultiSourceField(supplier, "address", multiSourceFieldModel)
        );
    }

    public static List<DynamicModelMapping> buildConditionConvert01() {
        AviatorModel aviatorModel = new AviatorModel.Builder("level(src)").
                addFunctions(new LevelTransfer()).build();
        return Lists.newArrayList(
                builder.buildConditionExp(supplier, "level", "level", aviatorModel)
        );
    }

    public static List<DynamicModelMapping> buildConditionConvert02() {
        AviatorModel aviatorModel = new AviatorModel.Builder("level(src)").
                addFunctions(new LevelTransfer()).build();
        return Lists.newArrayList(
                builder.buildConditionExp(supplier, "name.level", "name.level", aviatorModel)
        );
    }

    public static List<DynamicModelMapping> buildConditionConvert03() {
//        AviatorModel aviatorModel = new AviatorModel.Builder("level(src)").
//                addFunctions(new LevelTransfer()).build();
        Map<String, Object> map = new HashMap<>();
        map.put("expression", "level(src)");
        map.put("functions", null);
        map.put("env", null);
        return Lists.newArrayList(
                builder.buildConditionExp(supplier, "name.level", "name.level", map)
        );
    }

    public static List<DynamicModelMapping> buildDataModel() {
        return Lists.newArrayList(
                builder.buildFromSourceField(supplier, "customerNameDest", "customerNameSource"),
                builder.buildFromSourceField(supplier, "realNameDest", "realNameSource"),
                builder.buildFromSourceField(supplier, "sourceIdsDest.cidDest", "sourceIdsSource.cidSource")
        );

    }

    public static List<DynamicModelMapping> buildNestedMapping01() {
        return Lists.newArrayList(
                builder.buildFromSourceField(supplier, "province", "addressInfo.province"),
                builder.buildFromSourceField(supplier, "city", "addressInfo.city")
        );
    }

    public static List<DynamicModelMapping> buildNestedMapping02() {
        return Lists.newArrayList(
                builder.buildFromSourceField(supplier, "addressInfo.province", "addressInfo.province"),
                builder.buildFromSourceField(supplier, "addressInfo.city", "addressInfo.city")
        );
    }

    public static List<DynamicModelMapping> buildNestedMapping03() {
        return Lists.newArrayList(
                builder.buildFromSourceField(supplier, "addressInfo.province", "addressInfo.province"),
                builder.buildFromSourceField(supplier, "addressInfo.city", "addressInfo.city")
        );
    }
   public static List<DynamicModelMapping> buildNestedMapping04() {
        return Lists.newArrayList(
//                builder.buildFromSourceField(supplier, "addressInfo.province", "province"),
                builder.buildFromSourceField(supplier, "addressInfo.city", "city")
        );
    }


    static class LevelTransfer extends AbstractFunction {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject f1) {
            Object value = f1.getValue(env);
            try {
                Double levle = Double.valueOf("" + value);
                if (levle > 0 && levle < 51) {
                    return new AviatorString("A");
                } else if (levle > 50 && levle < 101) {
                    return new AviatorString("B");
                } else {
                    return new AviatorString("C");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return new AviatorString("未知");
        }

        @Override
        public String getName() {
            return "level";
        }
    }

}
