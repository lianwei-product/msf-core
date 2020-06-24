package cn.com.connext.msf.framework.json;


import cn.com.connext.msf.framework.query.QueryOperators;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class JsonOperater {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JsonOperater.class);

    public static boolean checkCombined(JsonNode jsonNode, FilterInfo filterInfo) {

        return checkCombined(jsonNode, filterInfo.getAnd(), filterInfo.getOr(),filterInfo.getNot());
    }

    /**
     * 根据条件判断数据是否满足要求
     *
     * @param jsonNode
     * @return
     */
    public static boolean checkCombined(JsonNode jsonNode, List<String> and, List<String> or, List<String> not) {

        return checkByType(jsonNode, and, "AND")
            && checkByType(jsonNode, or, "OR")
            && checkByType(jsonNode, not, "NOT");
    }

    public static boolean checkByType(JsonNode member, List<String> conditions, String conditionType) {
        if(CollectionUtils.isEmpty(conditions)){
            // 没有条件直接返回true
            return true;
        }
        for (String condition : conditions) {
            String[] args = condition.split("\\s+");
            if (args.length < 2) {
                log.warn("表达式语法错误: {}", condition);
                throw new RuntimeException("表达式语法错误:" + condition);
            }

            List<String> values = new ArrayList<>();
            findByPath(member, args[0], values);

            if (values.isEmpty()) {
                log.info("根据表达式 [{}] 找不到属性值", condition);
                return false;
            }

            log.info("根据表达式 [{}] 找到属性值: {}", condition, values);
            switch (conditionType) {
                case "AND":     // 必须满足所有条件
                    if (!decide(values, args[1], args[2])) {
                        return false;
                    }
                    break;
                case "OR":      // 必须满足一条条件
                    if (decide(values, args[1], args[2])) {
                        return true;
                    }
                    break;
                case "NOT":     // 必须不满足所有条件
                    if (decide(values, args[1], args[2])) {
                        return false;
                    }
                    break;
            }
        }
        return !conditionType.equals("OR");
    }

    /**
     * 判定
     *
     * @param field 字段值
     * @param op 逻辑运算符
     * @param value 目标比较值
     * @return 返回逻辑判定结果
     */
    public static boolean decide(List<String> field, String op, String value) {
        switch (op) {
            case QueryOperators.EQ:     // 所有属性都等于目标值
                return field.stream().filter(f -> f.equals(value)).count() == field.size();

            case QueryOperators.NE:     // 所有属性都不等于目标值
                return field.stream().filter(f -> f.equals(value)).count() == 0;

            case QueryOperators.IN:     // 属性包含目标值
                return field.stream().filter(f -> f.equals(value)).count() > 0;

            case QueryOperators.NIN:    // 属性不包含目标值
                return field.stream().filter(f -> f.equals(value)).count() == 0;

            case QueryOperators.LIKE:    // 属性包含目标值字符串
                return field.stream().filter(f -> f.matches("^.*"+value +".*$")).count() > 0;

            case QueryOperators.GT:     // 属性都大于目标值
                return field.stream().filter(f -> f.compareTo(value) > 0).count() == field.size();

            case QueryOperators.GE:     // 属性都大于等于目标值
                return field.stream().filter(f -> f.compareTo(value) >= 0).count() == field.size();

            case QueryOperators.LT:     // 属性都小于目标值
                return field.stream().filter(f -> f.compareTo(value) < 0).count() == field.size();

            case QueryOperators.LE:     // 属性都小于等于目标值
                return field.stream().filter(f -> f.compareTo(value) <= 0).count() == field.size();
            case QueryOperators.EMPTY:  // 属性判空
                return field.size() != 0 && Boolean.valueOf(value);
            default:
                log.warn("逻辑运算符不支持: {}", op);
        }
        return false;
    }

    /**
     * 根据path查询对应字段值
     *
     * @param json
     * @param field
     * @param result 返回查询结果，如果没有对应字段则返回空列表
     */
    public static void findByPath(JsonNode json, String field, List<String> result) {
        if (json == null) {
            return;
        }
        int index = field.indexOf('.');
        String node = field;
        if (index > 0) {
            node = node.substring(0, index);
        }
        json = json.get(node);
        if (json == null) {
            return;
        }
        if (json.isValueNode()) {
            switch (json.getNodeType()) {
                case NUMBER:
                    result.add(String.valueOf(json.numberValue()));
                    break;
                case STRING:
                    result.add(json.textValue());
                    break;
                default:
                    log.info("Other type: {}", json.getNodeType());
            }
            return;
        }
        if (json.isArray()) {
            for (JsonNode jsonNode : json) {
                if (jsonNode.isValueNode()) {
                    switch (jsonNode.getNodeType()) {
                        case NUMBER:
                            result.add(String.valueOf(jsonNode.numberValue()));
                            break;
                        case STRING:
                            result.add(jsonNode.textValue());
                            break;
                        default:
                            log.info("Other type: {}", jsonNode.getNodeType());
                    }
                } else {
                    findByPath(jsonNode, field.substring(index + 1), result);
                }
            }
        } else {
            findByPath(json, field.substring(index + 1), result);
        }
    }
}
