package cn.com.connext.msf.framework.entity;

import cn.com.connext.msf.framework.utils.Time;

/**
 * 生日范围模型
 */
public class TagConditionExpModel {

    private String exp;
    private String prefix;
    private long num;
    private String unit;

    public TagConditionExpModel() {

    }

    public static TagConditionExpModel from(String exp) {
        TagConditionExpModel model = new TagConditionExpModel();
        model.exp = exp;
        initData(exp, model);
        return model;
    }

    private static void initData(String exp, TagConditionExpModel model) {
        String unit = "d";
        for (String tmpUnit : Time.RELATIVE_TIME_UNITS) {
            if (exp.endsWith(tmpUnit)) {
                unit = tmpUnit;
                exp = exp.substring(0, exp.length() - 1);
                break;
            }
        }

        if (exp.contains("!")) {
            int endIndex = exp.length();
            exp = exp.substring(1, endIndex);
            model.prefix = "!";
        }

        model.num = Long.parseLong(exp);
        model.unit = unit;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
