package org.cc.dubbo.provider.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.cc.dubbo.api.FeedApi;
import org.cc.dubbo.enums.CommentType;
import org.cc.dubbo.pojo.*;
import org.cc.dubbo.util.CONSTS;
import org.cc.dubbo.vo.PageInfo;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public PageInfo<Publish> queryPublishList(Long userId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("date").descending());
        Query query = new Query().with(pageable);
        List<Timeline> timelines = mongoTemplate.find(query, Timeline.class, CONSTS.MONGODB_FEED_TIMELINE + "_" + userId);
        log.info("==========> timelines: {}", timelines);
        Criteria publishCriteria = Criteria
                .where("id")
                .in(timelines
                        .stream()
                        .map(Timeline::getPublishId).collect(Collectors.toList()));

        Query publishQuery = Query
                .query(publishCriteria)
                .with(Sort
                        .by("created")
                        .descending()
                );

        List<Publish> publishes = mongoTemplate.find(publishQuery, Publish.class);
        return new PageInfo<>(null, page, pageSize, publishes);
    }

    @Override
    public boolean saveComment(Long userId, String publishId, String commentContent) {
        return this.saveComment(userId, publishId, CommentType.COMMENT, commentContent);
    }

    @Override
    public boolean removeComment(Long userId, Long commentId) {
        try {
            Criteria criteria = Criteria.where("userId").is(userId).and("id").is(commentId);
            this.queryAndRemoveComment(criteria);
        } catch (Exception e) {
            log.error("remove comment error: {}", e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean removeNonTextComment(Long userId, String publishId, CommentType commentType) {
        try {
            Criteria criteria = Criteria
                    .where("userId").is(userId)
                    .and("publishId").is(publishId)
                    .and("commentType").is(commentType.getValue());
            this.queryAndRemoveComment(criteria);
        } catch (Exception e) {
            log.error("remove comment error: {}", e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean saveLikeComment(Long userId, String publishId) {
        return this.saveComment(userId, publishId, CommentType.LIKE, null);
    }

    @Override
    public boolean saveLoveComment(Long userId, String publishId) {
        return this.saveComment(userId, publishId, CommentType.FAVORITE, null);
    }

    @Override
    public Long queryCommentCount(String publishId, CommentType type) {
        long count = 0;
        try {
            Criteria criteria = Criteria
                    .where("publishId").is(publishId)
                    .and("commentType").is(type.getValue());
            Query query = Query.query(criteria);
            count = mongoTemplate.count(query, Comment.class);
        } catch (Exception e) {
            log.error("get comment count error: {} type:{}", e, type.getDesc());
        }
        return count;
    }

    private void queryAndRemoveComment(Criteria criteria) {
        Query query = Query.query(criteria);
        Comment comment = this.mongoTemplate.findOne(query, Comment.class);
        if (comment!=null) {
            this.mongoTemplate.remove(comment);
        }
    }

    private boolean saveComment(Long userId, String publishId, CommentType type, String content) {
        try {
            Comment comment = new Comment();
            comment.setId(ObjectId.get());
            comment.setPublishId(new ObjectId(publishId));
            comment.setCommentType(type.getValue());
            comment.setContent(content);
            comment.setCreated(System.currentTimeMillis());
            comment.setUserId(userId);
            this.mongoTemplate.save(comment);
            return true;
        } catch (Exception e) {
            log.error("save comment error: {}, commentType: {}", e, type.getDesc());
            e.printStackTrace();
            return false;
        }
    }
}
