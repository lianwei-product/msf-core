package cn.com.connext.msf.framework.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ObjectNodeTest {

    ObjectNode objectNode = null;

    public ObjectNodeTest() {
        ObjectNode memberNode = JsonNodeFactory.instance.objectNode();
        memberNode.put("user_name", "Mike");
        memberNode.put("mobile", "13776510001");

        ObjectNode address = JsonNodeFactory.instance.objectNode();
        address.put("add_detail", "上海路6号");
        address.put("add_Time1", "2020-01-22T14:47:50.78+08:00");
        address.put("add_Time2", "2020-01-22 14:47:50");
        address.put("add_int", "1");
        address.put("add_long", "2");
        address.put("add_double", "2.5");
        address.put("add_bool", "true");
        memberNode.set("address", address);

        objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.set("member", memberNode);
        System.out.println(objectNode);
    }

    @Test
    public void stringTest() throws Exception {
        String case1 = ObjectNodeUtil.getString(objectNode, "member.address.add_details");
        System.out.println("case1:" + case1);
        Assert.assertEquals("", case1);
        String case2 = ObjectNodeUtil.getString(objectNode, "member.address.add_detail");
        System.out.println("case2:" + case2);
        Assert.assertEquals("上海路6号", case2);
    }

    @Test
    public void stringOpTest() throws Exception {
        Optional<String> case1 = ObjectNodeUtil.getStringOp(objectNode, "member.address.add_details");
        System.out.println("case1:" + case1);
        Assert.assertEquals(Optional.empty(), case1);
        Optional<String> case2 = ObjectNodeUtil.getStringOp(objectNode, "member.address.add_detail");
        System.out.println("case2:" + case2);
        Assert.assertEquals("上海路6号", case2.get());
    }

    @Test
    public void intTest() throws Exception {
        Integer case1 = ObjectNodeUtil.getInt(objectNode, "member.address.add_ints");
        System.out.println("case1:" + case1);
        Assert.assertSame(0, case1);
        Integer case2 = ObjectNodeUtil.getInt(objectNode, "member.address.add_int");
        System.out.println("case2:" + case2);
        Assert.assertSame(1, case2);
    }

    @Test
    public void intTestOp() throws Exception {
        Optional<Integer> case1 = ObjectNodeUtil.getIntOp(objectNode, "member.address.add_ints");
        System.out.println("case1:" + case1);
        Assert.assertEquals(Optional.empty(), case1);
        Optional<Integer> case2 = ObjectNodeUtil.getIntOp(objectNode, "member.address.add_int");
        System.out.println("case2:" + case2);
        Assert.assertSame(1, case2.get());
    }

    @Test
    public void longTest() throws Exception {
        Long case1 = ObjectNodeUtil.getLong(objectNode, "member.address.add_longs");
        System.out.println("case1:" + case1);
        Assert.assertSame(0L, case1);
        Long case2 = ObjectNodeUtil.getLong(objectNode, "member.address.add_long");
        System.out.println("case2:" + case2);
        Assert.assertSame(2L, case2);
    }

    @Test
    public void longTestOp() throws Exception {
        Optional<Long> case1 = ObjectNodeUtil.getLongOp(objectNode, "member.address.add_longs");
        System.out.println("case1:" + case1);
        Assert.assertEquals(Optional.empty(), case1);
        Optional<Long> case2 = ObjectNodeUtil.getLongOp(objectNode, "member.address.add_long");
        System.out.println("case2:" + case2);
        Assert.assertSame(2L, case2.get());
    }

    @Test
    public void doubleTest() throws Exception {
        Double case1 = ObjectNodeUtil.getDouble(objectNode, "member.address.add_doubles");
        System.out.println("case1:" + case1);
        Assert.assertEquals("0.0", String.valueOf(case1.doubleValue()));
        Double case2 = ObjectNodeUtil.getDouble(objectNode, "member.address.add_double");
        System.out.println("case2:" + case2);
        Assert.assertEquals("2.5", String.valueOf(case2.doubleValue()));
    }

    @Test
    public void doubleTestOp() throws Exception {
        Optional<Double> case1 = ObjectNodeUtil.getDoubleOp(objectNode, "member.address.add_doubles");
        System.out.println("case1:" + case1);
        Assert.assertEquals(Optional.empty(), case1);
        Optional<Double> case2 = ObjectNodeUtil.getDoubleOp(objectNode, "member.address.add_double");
        System.out.println("case2:" + case2);
        Assert.assertEquals("2.5", String.valueOf(case2.get()));
    }

    @Test
    public void boolTest() throws Exception {
        Boolean case1 = ObjectNodeUtil.getBoolean(objectNode, "member.address.add_bools");
        System.out.println("case1:" + case1);
        Assert.assertFalse(case1);
        Boolean case2 = ObjectNodeUtil.getBoolean(objectNode, "member.address.add_bool");
        System.out.println("case2:" + case2);
        Assert.assertTrue(case2);

    }

    @Test
    public void boolTestOp() throws Exception {
        Optional<Boolean> case1 = ObjectNodeUtil.getBooleanOp(objectNode, "member.address.add_bools");
        System.out.println("case1:" + case1);
        Assert.assertEquals(Optional.empty(), case1);
        Optional<Boolean> case2 = ObjectNodeUtil.getBooleanOp(objectNode, "member.address.add_bool");
        System.out.println("case2:" + case2);
        Assert.assertTrue(case2.get());

    }

    @Test
    public void localDateTest() throws Exception {
        LocalDate case1 = ObjectNodeUtil.getLocalDate(objectNode, "member.address.add_Time1s");
        System.out.println("case1:" + case1);
        Assert.assertNull(case1);
        LocalDate case2 = ObjectNodeUtil.getLocalDate(objectNode, "member.address.add_Time1");
        System.out.println("case2:" + case2);
        Assert.assertEquals("2020-01-22", case2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void localDateTestOp() throws Exception {
        Optional<LocalDate> case1 = ObjectNodeUtil.getLocalDateOp(objectNode, "member.address.add_Time1s");
        System.out.println("case1:" + case1);
        Assert.assertEquals(Optional.empty(), case1);
        Optional<LocalDate> case2 = ObjectNodeUtil.getLocalDateOp(objectNode, "member.address.add_Time1");
        System.out.println("case2:" + case2);
        Assert.assertEquals("2020-01-22", case2.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void localDateTimeTest() throws Exception {
        LocalDateTime case1 = ObjectNodeUtil.getLocalDateTime(objectNode, "member.address.add_Time1s");
        System.out.println("case1:" + case1);
        Assert.assertNull(case1);
        LocalDateTime case2 = ObjectNodeUtil.getLocalDateTime(objectNode, "member.address.add_Time1");
        System.out.println("case2:" + case2);
        Assert.assertEquals("2020-01-22", case2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void localDateTimeTestOp() throws Exception {
        Optional<LocalDateTime> case1 = ObjectNodeUtil.getLocalDateTimeOp(objectNode, "member.address.add_Time1s");
        System.out.println("case1:" + case1);
        Assert.assertEquals(Optional.empty(), case1);
        Optional<LocalDateTime> case2 = ObjectNodeUtil.getLocalDateTimeOp(objectNode, "member.address.add_Time1");
        System.out.println("case2:" + case2);
        Assert.assertEquals("2020-01-22", case2.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void zonedDateTimeTest() throws Exception {
        ZonedDateTime case1 = ObjectNodeUtil.getZonedDateTime(objectNode, "member.address.add_Time1s");
        System.out.println("case1:" + case1);
        Assert.assertNull(case1);
        ZonedDateTime case2 = ObjectNodeUtil.getZonedDateTime(objectNode, "member.address.add_Time1");
        System.out.println("case2:" + case2);
        Assert.assertEquals("2020-01-22", case2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        ZonedDateTime case3 = ObjectNodeUtil.getZonedDateTime(objectNode, "member.address.add_Time2");
        System.out.println("case3:" + case3);
        Assert.assertEquals("2020-01-22", case3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }

    @Test
    public void zonedDateTimeTestOp() throws Exception {
        Optional<ZonedDateTime> case1 = ObjectNodeUtil.getZonedDateTimeOp(objectNode, "member.address.add_Time1s");
        System.out.println("case1:" + case1);
        Assert.assertEquals(Optional.empty(), case1);
        Optional<ZonedDateTime> case2 = ObjectNodeUtil.getZonedDateTimeOp(objectNode, "member.address.add_Time1");
        System.out.println("case2:" + case2);
        Assert.assertEquals("2020-01-22", case2.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        Optional<ZonedDateTime> case3 = ObjectNodeUtil.getZonedDateTimeOp(objectNode, "member.address.add_Time2");
        System.out.println("case3:" + case3);
        Assert.assertEquals("2020-01-22", case3.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }


    @Test
    public void delNodeByField() throws Exception {
        ObjectNode objectNode = this.objectNode.deepCopy();
        ObjectNode objectNode1 = ObjectNodeUtil.delNodeByField(objectNode, "member.address.add_bool");
        System.out.println(objectNode1.toString());
        System.out.println("==========================");
        Assert.assertNull(objectNode1.get("member").get("address").get("add_bool"));


        objectNode = this.objectNode.deepCopy();
        ObjectNode objectNode2 = ObjectNodeUtil.delNodeByField(objectNode, "member.address");
        System.out.println(objectNode2.toString());
        System.out.println("==========================");
        Assert.assertNull(objectNode2.get("member").get("address"));

        objectNode = this.objectNode.deepCopy();
        ObjectNode objectNode3 = ObjectNodeUtil.delNodeByField(objectNode, "member.mobile");
        System.out.println(objectNode3.toString());
        System.out.println("==========================");
        Assert.assertNull(objectNode3.get("member").get("mobile"));

        objectNode = this.objectNode.deepCopy();
        ObjectNode objectNode4 = ObjectNodeUtil.delNodeByField(objectNode, "member");
        System.out.println(objectNode4.toString());
        System.out.println("==========================");
        Assert.assertNull(objectNode4.get("member"));

    }


}