package org.cc.dubbo.provider.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.cc.dubbo.api.FeedApi;
import org.cc.dubbo.pojo.Album;
import org.cc.dubbo.pojo.Publish;
import org.cc.dubbo.pojo.Timeline;
import org.cc.dubbo.pojo.Users;
import org.cc.dubbo.util.CONSTS;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xmlunit.util.Linqy;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FeedApiTest {

    @DubboReference
    FeedApi feedApi;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testData() {
        mongoTemplate.save(new Users(ObjectId.get(), 1L, 2L, System.currentTimeMillis()));
        mongoTemplate.save(new Users(ObjectId.get(), 1L, 3L, System.currentTimeMillis()));
        mongoTemplate.save(new Users(ObjectId.get(), 1L, 4L, System.currentTimeMillis()));
        mongoTemplate.save(new Users(ObjectId.get(), 1L, 5L, System.currentTimeMillis()));
        mongoTemplate.save(new Users(ObjectId.get(), 1L, 6L, System.currentTimeMillis()));
    }

    @Test
    public void testSaveFeed() {
        Publish publish = new Publish();
        publish.setUserId(1L);
        publish.setLocationName("tj");
        publish.setSeeType(1);
        publish.setText("test txt");
        publish.setMedias(Arrays.asList("https://up.enterdesk.com/edpic/c5/3a/e5/c53ae5ec144c1d16105303c9d9bf68bb.jpg"));
        boolean result = this.feedApi.savePublish(publish);
        log.info("save publish: {}", result);
    }

    @Test
    public void testSaveResult() {
        Criteria criteria = Criteria.where("userId").is(1L);
        Query query = Query.query(criteria);
        Publish one = mongoTemplate.findOne(query, Publish.class);
        log.info("get saved publish: {}", one);


        Criteria albumCriteria = Criteria.where("publishId").is(one.getId());
        Query albumQuery = Query.query(albumCriteria);
        Album album = mongoTemplate.findOne(albumQuery, Album.class, CONSTS.MONGODB_FEED_ALBUM + "_" + 1L);
        log.info("get album success: {}", album);
        Criteria friendsCriteria = Criteria.where("userId").is(1L);
        Query friendsQuery = Query.query(friendsCriteria);
        List<Users> users = this.mongoTemplate.find(friendsQuery, Users.class);
        for (Users user: users) {
            Criteria timeLineCriteria = Criteria.where("userId").is(1L);
            Query timeLineQuery = Query.query(timeLineCriteria);
            Timeline timeline = this.mongoTemplate.findOne(timeLineQuery, Timeline.class, CONSTS.MONGODB_FEED_TIMELINE + "_" + user.getFriendId());
            log.info("find timeline: {}", timeline);
        }
    }
}
