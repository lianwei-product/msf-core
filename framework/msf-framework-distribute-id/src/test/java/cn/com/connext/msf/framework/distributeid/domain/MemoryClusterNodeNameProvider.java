package cn.com.connext.msf.framework.distributeid.domain;

import cn.com.connext.msf.framework.distributeid.provider.ClusterNodeNameProvider;

public class MemoryClusterNodeNameProvider implements ClusterNodeNameProvider {


    @Override
    public void init() {

    }

    @Override
    public void hold() {

    }

    @Override
    public void renew() {

    }

    @Override
    public String getClusterNodeName() {
        return "001";
    }

}
