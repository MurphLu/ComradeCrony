package org.cc.server.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cc.dubbo.api.FeedApi;
import org.cc.dubbo.pojo.Publish;
import org.cc.server.pojo.Feed;
import org.cc.server.pojo.User;
import org.cc.server.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class FeedService {

    @Autowired
    UserService userService;
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
}
