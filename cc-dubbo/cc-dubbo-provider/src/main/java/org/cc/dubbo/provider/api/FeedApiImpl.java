package org.cc.dubbo.provider.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.cc.dubbo.api.FeedApi;
import org.cc.dubbo.pojo.Album;
import org.cc.dubbo.pojo.Publish;
import org.cc.dubbo.pojo.Timeline;
import org.cc.dubbo.pojo.Users;
import org.cc.dubbo.util.CONSTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@DubboService
public class FeedApiImpl implements FeedApi {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean savePublish(Publish publish) {
        if(publish.getUserId() == null) {
            return false;
        }
        try {
            publish.setId(ObjectId.get());
            publish.setCreated(System.currentTimeMillis());
            publish.setSeeType(1);
            this.mongoTemplate.save(publish);
            log.info("save publish success");
            if(!CollectionUtils.isEmpty(publish.getMedias())) {
                Album album = new Album();
                album.setId(ObjectId.get());
                album.setPublishId(publish.getId());
                album.setCreated(System.currentTimeMillis());
                this.mongoTemplate.save(
                        album,
                        CONSTS.MONGODB_FEED_ALBUM + "_" + publish.getUserId()
                );
                log.info("save album success");
            }

            Criteria criteria = Criteria.where("userId").is(publish.getUserId());
            Query query = Query.query(criteria);
            List<Users> users = this.mongoTemplate.find(query, Users.class);
            users.add(new Users(null, publish.getUserId(), publish.getUserId(), 1L));
            for (Users user: users) {
                Timeline timeline = new Timeline();
                timeline.setId(ObjectId.get());
                timeline.setUserId(publish.getUserId());
                timeline.setPublishId(publish.getId());
                timeline.setDate(System.currentTimeMillis());
                this.mongoTemplate.save(timeline, CONSTS.MONGODB_FEED_TIMELINE + "_" + user.getFriendId());
            }
            log.info("save timeline success");
        } catch (Exception e) {
            log.error("save publish error: {}", e.toString());
            //TODO: 事务回滚
            return false;
        }
        return true;
    }
}
