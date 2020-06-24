package cn.com.connext.msf.framework.utils;

import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * 序号	说明	是否必填	允许填写的值	允许的符号
 * 1	秒	是	0－59	, - * /
 * 2	分	是	0－59	, - * /
 * 3	小时	是	0－23	, - * /
 * 4	日	是	1－31	, - * ? / L W
 * 5	月	是	1－12 or JAN-DEC	, - * /
 * 6	星期	是	1-7 or SUN-SAT	, - * ? / L #
 * 7	年	否	1970-2099	, - * /
 * <p>
 * 常用表达式例子
 * 　　（1）0 0 2 1 * ? *   表示在每月的1日的凌晨2点调整任务
 * 　　（2）0 15 10 ? * MON-FRI   表示周一到周五每天上午10:15执行作业
 * 　　（3）0 15 10 ? 6L 2002-2006   表示2002-2006年的每个月的最后一个星期五上午10:15执行作
 * 　　（4）0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
 * 　　（5）0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时
 * 　　（6）0 0 12 ? * WED    表示每个星期三中午12点
 * 　　（7）0 0 12 * * ?   每天中午12点触发
 * 　　（8）0 15 10 ? * *    每天上午10:15触发
 * 　　（9）0 15 10 * * ?     每天上午10:15触发
 * 　　（10）0 15 10 * * ? *    每天上午10:15触发
 * 　　（11）0 15 10 * * ? 2005    2005年的每天上午10:15触发
 * 　　（12）0 * 14 * * ?     在每天下午2点到下午2:59期间的每1分钟触发
 * 　　（13）0 0/5 14 * * ?    在每天下午2点到下午2:55期间的每5分钟触发
 * 　　（14）0 0/5 14,18 * * ?     在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
 * 　　（15）0 0-5 14 * * ?    在每天下午2点到下午2:05期间的每1分钟触发
 * 　　（16）0 10,44 14 ? 3 WED    每年三月的星期三的下午2:10和2:44触发
 * 　　（17）0 15 10 ? * MON-FRI    周一至周五的上午10:15触发
 * 　　（18）0 15 10 15 * ?    每月15日上午10:15触发
 * 　　（19）0 15 10 L * ?    每月最后一日的上午10:15触发
 * 　　（20）0 15 10 ? * 6L    每月的最后一个星期五上午10:15触发
 * 　　（21）0 15 10 ? * 6L 2002-2005   2002年至2005年的每月的最后一个星期五上午10:15触发
 * 　　（22）0 15 10 ? * 6#3   每月的第三个星期五上午10:15触发
 */
public class CronExpressionTest {
    @Test
    public void test01() throws ParseException {
        String expression = "0/1 0 2 * * ? 2019-2020"; // 2019-2020年 每天凌晨2点执行
        Assert.assertTrue(CronExpression.isValidExpression(expression));

        CronExpression cronExpression = new CronExpression(expression);
        Assert.assertTrue(cronExpression.isSatisfiedBy(Time.parseDate("2019-10-01 02:00:00")));
        Assert.assertTrue(cronExpression.isSatisfiedBy(Time.parseDate("2019-12-10 02:00:00")));

        Assert.assertFalse(cronExpression.isSatisfiedBy(Time.parseDate("2019-10-01 08:00:00")));
        Assert.assertFalse(cronExpression.isSatisfiedBy(Time.parseDate("2018-10-01 02:00:00")));

        Date current = new Date();
        System.out.println(current);
        System.out.println(cronExpression.getNextValidTimeAfter(current));
        System.out.println(cronExpression.getNextInvalidTimeAfter(current));

        expression = "0 0 0 * * ? 2019-2020"; // 2019-2020年 每天凌晨2点执行
        cronExpression = new CronExpression(expression);
        System.out.println(cronExpression.getNextInvalidTimeAfter(current));
    }

    @Test
    public void test02() throws ParseException {
        String expression = "0 0 * * * ?"; // 每小时执行
        Assert.assertTrue(CronExpression.isValidExpression(expression));

        CronExpression cronExpression = new CronExpression(expression);
        Assert.assertTrue(cronExpression.isSatisfiedBy(Time.parseDate("2019-10-01 02:00:00")));
        Assert.assertTrue(cronExpression.isSatisfiedBy(Time.parseDate("2019-12-10 02:00:00")));

        Date current = new Date();
        System.out.println(current);
        System.out.println(cronExpression.getNextValidTimeAfter(current));
        System.out.println(cronExpression.getNextInvalidTimeAfter(current));
    }

    @Test
    public void test03() throws ParseException {
        String expression = "0 0 * * * ?"; // 每小时执行
        int testCount = 10000;

        long beginTime = System.currentTimeMillis();
        boolean lastResult = false;
        for (int i = 0; i < testCount; i++) {
            CronExpression cronExpression = new CronExpression(expression);
            lastResult = cronExpression.isSatisfiedBy(new Date());
        }
        long took = System.currentTimeMillis() - beginTime;
        System.out.println(MessageFormat.format("Took: {0}ms, last result:", took, lastResult));
    }

}