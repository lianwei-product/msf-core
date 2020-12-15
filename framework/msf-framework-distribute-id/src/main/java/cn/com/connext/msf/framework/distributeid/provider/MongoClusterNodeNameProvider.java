package cn.com.connext.msf.framework.distributeid.provider;

import cn.com.connext.msf.framework.distributeid.entity.ClusterNodeRenewInfo;
import cn.com.connext.msf.framework.distributeid.exception.NodeNameException;
import cn.com.connext.msf.framework.distributeid.exception.NodeNameNotHoldException;
import cn.com.connext.msf.framework.distributeid.repository.ClusterNodeRenewInfoRepository;
import cn.com.connext.msf.framework.utils.Base58UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class MongoClusterNodeNameProvider implements ClusterNodeNameProvider {

    private final Logger logger = LoggerFactory.getLogger(MongoClusterNodeNameProvider.class);

    private final String owner;

    /**
     * 节点超时时间（单位：秒）
     * 采用以天为单位生成标识时，超时时间默认值不可以小于24小时，避免应用重启后，所生成的标识重复。
     */
    private final int expiresTimeSpan;
    private final ClusterNodeRenewInfoRepository repository;

    private int clusterNodeName;
    private int clusterNodeNameLength;
    private boolean renewSuccess = false;
    private Exception lastError = new NodeNameNotHoldException();

    public MongoClusterNodeNameProvider(@Value("${msf.distribute-id.max-node-count:999}") int maxNodeCount,
                                        @Value("${msf.distribute-id.expire-time-span:86400}") int expiresTimeSpan,
                                        ClusterNodeRenewInfoRepository repository) {
        this.owner = Base58UUID.newBase58UUID();
        this.expiresTimeSpan = expiresTimeSpan;
        this.repository = repository;
        this.clusterNodeNameLength = Integer.toString(maxNodeCount).length();
    }

    /**
     * 检测MongoDB中，collection是否已经存在，如果不存在则创建。
     */
    @Override
    public void init() {
        if (repository.init()) {
            logger.info("Init distributeId mongoDB collection success");
        }
    }

    @Override
    public void hold() {
        ClusterNodeRenewInfo expireRenewInfo = repository.findExpireRenewInfo();
        if (expireRenewInfo != null) {
            ClusterNodeRenewInfo renewInfo = ClusterNodeRenewInfo.from(expireRenewInfo.getName(), owner, expiresTimeSpan);
            renewSuccess = repository.update(renewInfo);
            if (renewSuccess) {
                clusterNodeName = renewInfo.getName();
                logger.info("Hold expire distributeId cluster node name success. Cluster node name: {}.", clusterNodeName);
            } else {
                hold();
            }
        } else {
            int newNodeName = repository.findNewNodeName();
            ClusterNodeRenewInfo renewInfo = ClusterNodeRenewInfo.from(newNodeName, owner, expiresTimeSpan);
            renewSuccess = repository.create(renewInfo);
            if (renewSuccess) {
                clusterNodeName = renewInfo.getName();
                logger.info("Hold new distributeId cluster node name success. Cluster node name: {}.", clusterNodeName);
            } else {
                hold();
            }
        }
    }

    @Override
    public void renew() {
        try {
            ClusterNodeRenewInfo renewInfo = ClusterNodeRenewInfo.from(clusterNodeName, owner, expiresTimeSpan);
            renewSuccess = repository.update(renewInfo);
            if (renewSuccess) {
                lastError = null;
                logger.debug("Renew distributeId cluster node name success. Cluster node name: {}.", clusterNodeName);
            } else {
                logger.error("Renew distributeId cluster node name error, maybe other instance hold current node name. Cluster node name: {}.", clusterNodeName);
            }
        } catch (Exception e) {
            logger.error("DistributeId cluster node name renew error.", e);
            lastError = e;
        }
    }

    @Override
    public String getClusterNodeName() {
        if (renewSuccess) {
            return StringUtils.leftPad(Integer.toString(clusterNodeName), clusterNodeNameLength, "0");
        }

        throw new NodeNameException(lastError);
    }


}
