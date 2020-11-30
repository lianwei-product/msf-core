package cn.com.connext.msf.framework.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: ming.wang
 * @Date: 2020/8/31 15:12
 * @Description: 模板解析工具类    其中regx表达式需用{}包起来。例如类中默认的表达式。也可以使用个性化的正则表达式
 */
public class StringTemplateParser {

    public static String parse(String template, Map<String, String> params) {
        return parse(MatcherContext.defautRegx, template, params);
    }

    public static String parse(String regx, String template, Map<String, String> params) {
        Matcher matcher = MatcherContext.getMatcher(regx, template);
        return doReplace(params, matcher);
    }

    public static String parse(String template, ObjectNode params) {
        return parse(MatcherContext.defautRegx, template, params);
    }

    public static String parse(String regx, String template, ObjectNode params) {
        List<String> keys = getKeys(regx, template);
        return parse(regx, template, buildMap(keys, params));
    }

    public static List<String> getKeys(String template) {
        Matcher matcher = MatcherContext.getMatcher(MatcherContext.defautRegx, template);
        return getKeys(matcher);
    }

    public static List<String> getKeys(String regx, String template) {
        Matcher matcher = MatcherContext.getMatcher(regx, template);
        return getKeys(matcher);
    }

    private static String doReplace(Map<String, String> params, Matcher matcher) {
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String param = matcher.group();
            Object value = params.get(param.substring(1, param.length() - 1));
            matcher.appendReplacement(sb, value == null ? "" : value.toString());
        }
        matcher.appendTail(sb);
        String result = sb.toString();
        matcher.reset();
        return result;
    }

    private static Map<String, String> buildMap(List<String> keys, ObjectNode objectNode) {
        Map<String, String> map = Maps.newHashMap();
        for (String fieldName : keys) {
            map.put(fieldName, ObjectNodeUtil.getString(objectNode, fieldName));
        }
        return map;
    }

    private static List<String> getKeys(Matcher matcher) {
        List<String> params = Lists.newArrayList();
        while (matcher.find()) {
            String param = matcher.group();
            params.add(param.substring(1, param.length() - 1));
        }
        matcher.reset();
        return params;
    }

    static class MatcherContext {
        private static final Map<String, Matcher> matcherCache;
        private static final Map<String, Pattern> patternCache;
        private static final String defautRegx = "\\{[a-zA-Z0-9._]*\\}";

        static {
            matcherCache = Maps.newHashMap();
            patternCache = Maps.newHashMap();
            patternCache.put(defautRegx, Pattern.compile(defautRegx));
        }

        static Matcher getMatcher(String regx, String template) {
            Matcher matcher = matcherCache.get(template + "_" + regx);
            if (null == matcher) {
                synchronized (MatcherContext.class) {
                    if (null == matcher) {
                        matcher = matcherCache.get(template + "_" + regx);
                        if (null == matcher) {
                            Pattern pattern = patternCache.get(regx);
                            if (null == pattern) {
                                pattern = Pattern.compile(regx);
                                patternCache.put(regx, pattern);
                            }
                            matcher = pattern.matcher(template);
                            matcherCache.put(template + "_" + regx, matcher);
                        }
                    }
                }
            }
            return matcher;
        }
    }
}
