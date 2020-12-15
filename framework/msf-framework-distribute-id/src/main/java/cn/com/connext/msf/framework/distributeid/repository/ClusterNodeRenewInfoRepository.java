package cn.com.connext.msf.framework.distributeid.repository;

import cn.com.connext.msf.framework.distributeid.entity.ClusterNodeRenewInfo;
import cn.com.connext.msf.framework.distributeid.exception.NodeNameAllInUseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;

public class ClusterNodeRenewInfoRepository {

    private final int maxNodeCount;
    private final MongoOperations mongoOperations;

    public ClusterNodeRenewInfoRepository(@Value("${msf.distribute-id.max-node-count:999}") int maxNodeCount,
                                          MongoOperations mongoOperations) {
        this.maxNodeCount = maxNodeCount;
        this.mongoOperations = mongoOperations;
    }

    /**
     * 初始化，检测数据库表是否需要建立等操作
     */
    public boolean init() {
        if (!mongoOperations.collectionExists(ClusterNodeRenewInfo.class)) {
            mongoOperations.createCollection(ClusterNodeRenewInfo.class);
            return true;
        }
        return false;
    }

    public boolean create(ClusterNodeRenewInfo renewalInfo) {
        try {
            mongoOperations.insert(renewalInfo);
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    public boolean update(ClusterNodeRenewInfo renewalInfo) {
        Criteria criteria = Criteria.where("name").is(renewalInfo.getName())
                .andOperator(
                        new Criteria().orOperator(
                                new Criteria("owner").is(renewalInfo.getOwner()),
                                new Criteria("expires").lt(new Date())
                        )
                );

        Update update = new Update()
                .set("owner", renewalInfo.getOwner())
                .set("expires", renewalInfo.getExpires());

        return mongoOperations.updateFirst(new Query(criteria), update, ClusterNodeRenewInfo.class).getModifiedCount() > 0;
    }

    /**
     * 获取已经过期的节点续约信息
     */
    public ClusterNodeRenewInfo findExpireRenewInfo() {
        Query query = new Query(Criteria.where("expires").lt(new Date())).with(Sort.by(Sort.Direction.ASC, "expires"));
        return mongoOperations.findOne(query, ClusterNodeRenewInfo.class);
    }

    /**
     * 从MongoDB中查询出一个新的可用节点名称，节点名称必须大于0小于maxNodeName
     */
    public int findNewNodeName() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "_id"));
        ClusterNodeRenewInfo renewalInfo = mongoOperations.findOne(query, ClusterNodeRenewInfo.class);
        if (renewalInfo == null) {
            return 1;
        }

        if (renewalInfo.getName() >= maxNodeCount) {
            throw new NodeNameAllInUseException();
        }

        return renewalInfo.getName() + 1;
    }
}