package cn.com.connext.msf.framework.mapping.aviator;

import cn.com.connext.msf.framework.aviator.functions.AviatorTransfer;
import cn.com.connext.msf.framework.mapping.aviator.method.MyReplace;
import cn.com.connext.msf.framework.mapping.model.AviatorModel;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AviatorRuleEngineTest {

    @Test
    public void test01() {
        //注册自定义表达式函数
        AviatorEvaluator.addFunction(new FieldsFunction());
        AviatorEvaluator.addFunction(new RedisCountFunction());

        //用户指定规则
        String expression = "redisCount('1','hour',fields(userid,ip,action)) >= 100";
//        Expression compiledExp = AviatorEvaluator.compile(expression);

        //运行时收到数据
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("userid", "9527");
        fields.put("ip", "127.0.0.1");
        fields.put("phone", "18811223344");
        fields.put("action", "click");

//        Boolean needAlarm = (Boolean) compiledExp.execute(fields);
        Boolean needAlarm = (Boolean) AviatorEvaluator.execute(expression, fields);

        if (needAlarm) {
            System.out.printf("报警");
        }
        Assert.assertEquals(true, needAlarm);
    }

    @Test
    public void test02() {
        String expression = "redisCount('1','hour',fields(userid,ip,action)) >= 100";

        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("userid", "9527");
        env.put("ip", "127.0.0.1");
        env.put("phone", "18811223344");
        env.put("action", "click");

        AviatorModel build = new AviatorModel.Builder(expression)
                .addFunctions(new FieldsFunction())
                .addFunctions(new RedisCountFunction())
                .setEnv(env).build();

        System.out.println(build);
        Boolean needAlarm = (Boolean) build.excute();
        System.out.println(needAlarm);
        Assert.assertEquals(true, needAlarm);

    }

    @Test
    public void test03() throws NoSuchMethodException, IllegalAccessException {
//        AviatorEvaluator.addStaticFunctions("mr",MyReplace.class);
        AviatorEvaluator.importFunctions(MyReplace.class);
        String expression = "MyReplace.myReplace(src,'d','y')";

        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("src", "hello world!");

        AviatorModel build = new AviatorModel.Builder(expression)
//                .addFunctions(new MyReplace())
                .setEnv(env).build();

        System.out.println(build);
        System.out.println(build.excute());
        Assert.assertEquals("hello worly!", build.excute());

    }

    @Test
    public void test04() {
        String expression = "str(true)";
        AviatorModel build = new AviatorModel.Builder(expression)
                .build();

        System.out.println(build);
        System.out.println(build.excute());
        Assert.assertEquals("true", build.excute());

    }

    @Test
    public void test05() {
        String expression = "str(src)";
        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("src", "hello world!");

        AviatorModel build = new AviatorModel.Builder(expression)
                .setEnv(env)
                .build();

        System.out.println(build);
        System.out.println(build.excute());
        Assert.assertEquals("hello world!", build.excute());

    }

    @Test
    public void test06() {
        String expression = "str('src')";
        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("src", "hello world!");

        AviatorModel build = new AviatorModel.Builder(expression)
                .setEnv(env)
                .build();

        System.out.println(build);
        System.out.println(build.excute());
        Assert.assertEquals("src", build.excute());

    }

    @Test
    public void test07() {
        String expression = "seq.list('as','bs','cs')";
        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("src", "hello world!");

        AviatorModel build = new AviatorModel.Builder(expression)
                .setEnv(env)
                .build();

        System.out.println(build);
        System.out.println(build.excute());
//        Assert.assertEquals("src", build.excute());

    }

    @Test
    public void test08() {
        String expression = "seq.map(" +
                "'A',seq.map('0','1000')," +
                "'B',seq.map('1001','2000')," +
                "'C',seq.map('2001','3000')" +
                ")";
        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("src", "hello world!");

        AviatorModel build = new AviatorModel.Builder(expression)
                .setEnv(env)
                .build();

        System.out.println(build);
        Object excute = build.excute();
        System.out.println(excute);
    }

    @Test
    public void test09() throws NoSuchMethodException, IllegalAccessException {
        AviatorEvaluator.importFunctions(MyReplace.class);
        String expression = "MyReplace.transfer(MyReplace.getInstance()," +
                "src,seq.map(" +
                "'A',seq.map('0','1000'),'B',seq.map('1001','2000'),'C',seq.map('2001','3000')" +
                ")" +
                ")";
        AviatorEvaluator.validate(expression);
        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("src", "100");

        AviatorModel build = new AviatorModel.Builder(expression)
                .setEnv(env).build();
        String excute = (String) build.excute();
        System.out.println(excute);
        Assert.assertEquals("A", build.excute());

    }

    @Test
    public void test10() throws NoSuchMethodException, IllegalAccessException {
        AviatorEvaluator.importFunctions(AviatorTransfer.class);
        String expression = "AviatorTransfer.transfer(" +
                "src,seq.map(" +
                "'A',seq.map('0','1000'),'B',seq.map('1001','2000'),'C',seq.map('2001','3000')" +
                ")" +
                ")";
        AviatorEvaluator.validate(expression);
        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("src", "1500");

        AviatorModel build = new AviatorModel.Builder(expression)
                .setEnv(env).build();
        String excute = (String) build.excute();
        System.out.println(excute);
        Assert.assertEquals("B", build.excute());

    }

    @Test
    public void test11() throws NoSuchMethodException, IllegalAccessException {
        AviatorEvaluator.addInstanceFunctions("s", String.class);
        String expression = "s.indexOf('hello','l')";
        Expression compile = AviatorEvaluator.compile(expression);
        //运行时收到数据
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("src", "100");

        AviatorModel build = new AviatorModel.Builder(expression)
                .setEnv(env).build();

        Object excute = build.excute();
        System.out.println(excute);
        Assert.assertEquals(2L, excute);


    }

//    static class MyReplace extends AbstractFunction {
//
//        @Override
//        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
//            //获取可变参数
//            String src = ""+arg1.getValue(env);
//            String p = FunctionUtils.getStringValue(arg2, env);
//            String t = FunctionUtils.getStringValue(arg3, env);
//
//            System.out.println("MyReplaceFunction : ");
//
//            return new AviatorString( src.replace(p, t));
//        }
//
//        @Override
//        public String getName() {
//            return "myReplace";
//        }
//    }

    static class FieldsFunction extends AbstractFunction {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject f1, AviatorObject f2, AviatorObject f3) {
            //获取可变参数
            StringBuilder redisKey = new StringBuilder();
            Object value1 = f1.getValue(env);
            Object value2 = f2.getValue(env);
            Object value3 = f3.getValue(env);
            if (value1 != null && value2 != null && value3 != null) {
                redisKey.append(value1.toString());
                redisKey.append(value2.toString());
                redisKey.append(value3.toString());
            } else {
                //TODO 参数合法性校验
            }
            redisKey.append(":");
            return new AviatorString(redisKey.toString());
        }

        @Override
        public String getName() {
            return "fields";
        }
    }


    static class RedisCountFunction extends AbstractFunction {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {

            String period = FunctionUtils.getStringValue(arg1, env);
            String timeUnit = FunctionUtils.getStringValue(arg2, env);
            String redisKey = FunctionUtils.getStringValue(arg3, env);

            System.out.println("RedisCountFunction : " + period + " , " + timeUnit + " , " + redisKey);

            long redisCount = redisGetAndIncrease(redisKey);

            return AviatorLong.valueOf(redisCount);
        }

        private int redisGetAndIncrease(String redisKey) {
            System.out.println("get redis : " + redisKey);
            //这里查询redis获得活动的值；
            return 10000;
        }

        @Override
        public String getName() {
            return "redisCount";
        }
    }

}
