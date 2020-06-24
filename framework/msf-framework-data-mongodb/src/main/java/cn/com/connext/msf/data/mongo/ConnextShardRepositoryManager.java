package cn.com.connext.msf.data.mongo;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ConnextShardRepositoryManager {

    private final List<ConnextShardRepository> repositoryList;

    public ConnextShardRepositoryManager(@Autowired(required = false) List<ConnextShardRepository> repositoryList) {
        this.repositoryList = repositoryList == null ? Lists.newArrayList() : repositoryList;
    }

    public void init(String shardCode) {
        repositoryList.forEach(repository -> repository.initShard(shardCode));
    }

    public void drop(String shardCode) {
        repositoryList.forEach(repository -> repository.dropShard(shardCode));
    }
}
