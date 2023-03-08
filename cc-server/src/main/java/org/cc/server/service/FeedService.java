package org.cc.server.service;

import com.alibaba.fastjson2.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.cc.dubbo.api.FeedApi;
import org.cc.dubbo.enums.CommentType;
import org.cc.dubbo.pojo.Comment;
import org.cc.dubbo.pojo.Publish;
import org.cc.dubbo.vo.PageInfo;
import org.cc.server.pojo.Feed;
import org.cc.server.pojo.TimelineFeed;
import org.cc.server.pojo.User;
import org.cc.server.pojo.UserInfo;
import org.cc.server.utils.UserThreadLocal;
import org.cc.server.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeedService {

    @Autowired
    UserInfoService userInfoService;

    @DubboReference
    FeedApi feedApi;
    public Boolean saveFeed(Feed feed) {
        try {
            User user = UserThreadLocal.get();
            log.info("get user info: {}", user);
            Publish publish = new Publish();
            publish.setUserId(user.getId());
            publish.setText(feed.getTextContent());
            publish.setLocationName(feed.getLocation());
            publish.setLatitude(feed.getLatitude());
            publish.setLongitude(feed.getLongitude());
            //TODO save images
            publish.setMedias(Arrays.asList("https://up.enterdesk.com/edpic/c5/3a/e5/c53ae5ec144c1d16105303c9d9bf68bb.jpg"));
            log.info("to publish feed: {}", publish);
            this.feedApi.savePublish(publish);
        } catch (Exception e) {
            log.error("save feed error: {}", e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public PageResult queryPages(Long userId, int page, int pageSize) {
        PageInfo<Publish> publishPageInfo = feedApi.queryPublishList(userId, page, pageSize);

        List<Publish> publishes = publishPageInfo.getRecords();
        List<Long> ids = publishes.stream().map(Publish::getUserId).collect(Collectors.toList());

        List<UserInfo> userInfoList = userInfoService.listByIds(ids);

        List<TimelineFeed> timelineFeeds = publishes.stream().map(publish -> {
            Optional<UserInfo> optUserInfo = userInfoList
                    .stream()
                    .filter(item -> Objects.equals(item.getUserId(), publish.getUserId()))
                    .findAny();
            if (!optUserInfo.isPresent()) {
                return null;
            }
            UserInfo userInfo = optUserInfo.get();
            TimelineFeed timelineFeed = new TimelineFeed();
            timelineFeed.setId(publish.getId().toHexString());
            timelineFeed.setUserId(publish.getUserId());
            timelineFeed.setLoveCount(100);
            timelineFeed.setLikeCount(80);
            timelineFeed.setDistance("12km");
            timelineFeed.setHasLoved(1);
            timelineFeed.setHasLiked(1);
            timelineFeed.setCommentCount(10);
            timelineFeed.setCreateDate(DateUtils.formatYMD8(publish.getCreated(), ZoneId.systemDefault()));
            timelineFeed.setTextContent(publish.getText());
            timelineFeed.setImageContent(publish.getMedias().toArray(new String[0]));
            timelineFeed.setAvatar(userInfo.getLogo());
            timelineFeed.setAge(userInfo.getAge());
            timelineFeed.setGender(userInfo.getSex().getDesc());
            timelineFeed.setTags(StringUtils.split(userInfo.getTags(), ","));
            timelineFeed.setNickname(userInfo.getNickName());
            return timelineFeed;
        }).collect(Collectors.toList());
        List<TimelineFeed> records = timelineFeeds.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return new PageResult(null, pageSize, null, page, records);
    }

    public boolean addComment(Comment comment) {
        User user = UserThreadLocal.get();
        CommentType commentType = CommentType.withValue(comment.getCommentType());
        if(commentType==null) {
            return false;
        }
        try {
            switch (commentType) {
                case LIKE:
                    feedApi.saveLikeComment(user.getId(), comment.getPublishId().toString());
                    return true;
                case COMMENT:
                    feedApi.saveComment(user.getId(), comment.getPublishId().toString(), comment.getContent());
                    return true;
                case FAVORITE:
                    feedApi.saveLoveComment(user.getId(), comment.getPublishId().toString());
                    return true;
            }
        } catch (Exception e) {
            log.error("save comment error: {}", e.toString());
            e.printStackTrace();
        }
        return false;
    }

    public Long getCommentCount(Comment comment) {
        return feedApi.queryCommentCount(comment.getPublishId().toString(), CommentType.withValue(comment.getCommentType()));
    }
}
