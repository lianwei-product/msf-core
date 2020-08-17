package cn.com.connext.msf.framework.data.elasticsearch.utils;

import java.util.*;

public class Region {
    private final static Random random;
    private final static List<String> provinceList;
    private final static List<List<String>> cityList;
    private final static List<String> roadList;

    static {
        random = new Random();
        provinceList = new ArrayList<>(Arrays.asList("江苏", "浙江", "广东", "上海", "北京"));
        cityList = new ArrayList<>();
        cityList.add(new ArrayList<>(Arrays.asList("南京", "苏州", "无锡", "常州")));
        cityList.add(new ArrayList<>(Arrays.asList("杭州", "温州", "湖州", "宁波")));
        cityList.add(new ArrayList<>(Arrays.asList("广州", "深圳", "珠海", "汕头")));
        cityList.add(new ArrayList<>(Arrays.asList("黄浦", "徐汇", "长宁", "静安")));
        cityList.add(new ArrayList<>(Arrays.asList("朝阳", "丰台", "海淀", "房山")));

        roadList = new ArrayList<>(Arrays.asList("湖东路", "至善路", "格致路", "中山", "应天大街",
                "珠江路", "新街口", "土山路", "湖山路", "丽泽路"));
    }

    private String province;
    private String city;

    public static Region buildRegion() {
        Region region = new Region();
        int p = random.nextInt(provinceList.size());
        region.province = provinceList.get(p);

        List<String> cities = cityList.get(p);
        int c = random.nextInt(cities.size());
        region.city = cities.get(c);

        return region;
    }


    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }




}
