package cn.com.connext.msf.framework.mapping.aviator.method;

import cn.com.connext.msf.framework.aviator.AviatorImportFunction;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Author: ming.wang
 * @Date: 2020/10/27 0027 10:22
 * @Description:
 */
//@Import(ns = "mr")
//@Import()
public class MyReplace implements AviatorImportFunction {
    public static AviatorImportFunction instance = new MyReplace();

    //    @Override
//    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
//        //获取可变参数
//        String src = ""+arg1.getValue(env);
//        String p = FunctionUtils.getStringValue(arg2, env);
//        String t = FunctionUtils.getStringValue(arg3, env);
//
//        System.out.println("MyReplaceFunction : ");
//
//        return new AviatorString( src.replace(p, t));
//    }
//
//    @Override
//    public String getName() {
//        return "myReplace";
//    }
    public static String myReplace(String src, String pattern, String target) {
        return src.replace(pattern, target);
    }

    public String transfer(String src, Map<String, Map<String, String>> map) {
        try {
            if (!CollectionUtils.isEmpty(map)) {
                Double srcDou = Double.valueOf("" + src);
                for (Map.Entry<String, Map<String, String>> target : map.entrySet()) {
                    String key = target.getKey();
                    Map<String, String> value = target.getValue();
                    for (Map.Entry<String, String> child : value.entrySet()) {
                        Double start = Double.valueOf("" + child.getKey());
                        Double end = Double.valueOf("" + child.getValue());
                        if (srcDou >= start && srcDou <= end) {
                            return key;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "UNKNOWN";
    }

     public static AviatorImportFunction getInstance() {
        return instance ;
    }
}

