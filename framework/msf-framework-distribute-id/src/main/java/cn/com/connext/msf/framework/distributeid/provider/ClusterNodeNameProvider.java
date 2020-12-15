package cn.com.connext.msf.framework.distributeid.provider;

public interface ClusterNodeNameProvider {

    /**
     * 初始化，检测数据库表是否需要建立等操作
     */
    void init();

    /**
     * 获取节点并开始占用
     */
    void hold();

    /**
     * 节点续约
     */
    void renew();

    /**
     * 获取节点名称，默认应返回2位数字格式字符串，最小为01，最高为99
     */
    String getClusterNodeName();

}
