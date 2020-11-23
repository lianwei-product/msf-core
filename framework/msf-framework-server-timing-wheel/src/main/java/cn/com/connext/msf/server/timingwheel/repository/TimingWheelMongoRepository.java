package cn.com.connext.msf.server.timingwheel.repository;

import cn.com.connext.msf.server.timingwheel.entity.TimingWheelClusterLock;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriodUpdateTag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

public class TimingWheelMongoRepository implements TimingWheelRepository {

    private final MongoOperations mongoOperations;
    private final String timingWheelIdentify;

    public TimingWheelMongoRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
        this.timingWheelIdentify = "timing-wheel";
    }

    @Override
    public void initialLock(String owner) {
        Query query = Query.query(Criteria.where("id").is(timingWheelIdentify));
        Update update = new Update()
                .setOnInsert("owner", owner)
                .setOnInsert("expires", new Date(0));

        mongoOperations.upsert(query, update, TimingWheelClusterLock.class);
    }

    @Override
    public boolean acquireLock(String owner, Date expiresTime) {
        Criteria criteria = Criteria.where("id").is(timingWheelIdentify)
                .andOperator(new Criteria().orOperator(new Criteria("owner").is(owner), new Criteria("expires").lt(new Date())));

        Update update = new Update()
                .set("owner", owner)
                .set("expires", expiresTime);

        return mongoOperations.updateFirst(new Query(criteria), update, TimingWheelClusterLock.class).getModifiedCount() > 0;
    }

    @Override
    public void releaseLock(String owner) {
        Criteria criteria = Criteria.where("id").is(timingWheelIdentify).and("owner").is(owner);
        Update update = new Update().set("expires", new Date(0));  // 立即过期
        mongoOperations.updateFirst(new Query(criteria), update, TimingWheelClusterLock.class);
    }

    @Override
    public void appendEvent(TimingWheelEvent timingWheelEvent) {
        mongoOperations.save(timingWheelEvent);
    }

    @Override
    public void appendPeriod(TimingWheelPeriod timingWheelPeriod) {
        mongoOperations.save(timingWheelPeriod);
        updateLatestPeriodUpdateTag();
    }

    @Override
    public void removePeriodInfo(String id) {
        TimingWheelPeriod periodInfo = mongoOperations.findById(id, TimingWheelPeriod.class);
        if (periodInfo != null) {
            mongoOperations.remove(periodInfo);
            updateLatestPeriodUpdateTag();
        }
    }

    @Override
    public List<TimingWheelEvent> takeEventList(int size) {
        Query query = Query.query(Criteria.where("eventTime").lte(new Date())).with(PageRequest.of(0, size, Sort.Direction.ASC, "eventTime"));
        return mongoOperations.findAllAndRemove(query, TimingWheelEvent.class);
    }


    @Override
    public List<TimingWheelPeriod> takePeriodList() {
        Date currentTime = new Date();
        Query query = Query.query(Criteria.where("startTime").lte(currentTime).and("endTime").gte(currentTime));
        return mongoOperations.find(query, TimingWheelPeriod.class);
    }

    @Override
    public TimingWheelPeriod findPeriodInfo(String id) {
        return mongoOperations.findById(id, TimingWheelPeriod.class);
    }

    @Override
    public void initLatestPeriodUpdateTag() {
        Query query = Query.query(Criteria.where("id").is(timingWheelIdentify));
        Update update = new Update().setOnInsert("updateTag", new Date().getTime());
        mongoOperations.upsert(query, update, TimingWheelPeriodUpdateTag.class);
    }

    @Override
    public long findLatestPeriodUpdateTag() {
        TimingWheelPeriodUpdateTag tagInfo = mongoOperations.findById(timingWheelIdentify, TimingWheelPeriodUpdateTag.class);
        if (tagInfo == null)
            return 0;
        else
            return tagInfo.getUpdateTag();
    }

    @Override
    public void updateLatestPeriodUpdateTag() {
        TimingWheelPeriodUpdateTag timingWheelPeriodUpdateTag = new TimingWheelPeriodUpdateTag();
        timingWheelPeriodUpdateTag.setId(timingWheelIdentify);
        timingWheelPeriodUpdateTag.setUpdateTag(new Date().getTime());
        mongoOperations.save(timingWheelPeriodUpdateTag);
    }

}
