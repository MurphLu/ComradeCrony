package org.cc.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.cc.dubbo.pojo.Comment;
import org.cc.dubbo.vo.PageInfo;
import org.cc.server.pojo.Feed;
import org.cc.server.pojo.TimelineFeed;
import org.cc.server.pojo.User;
import org.cc.server.service.FeedService;
import org.cc.server.utils.UserThreadLocal;
import org.cc.server.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("feed")
public class FeedController {
    @Autowired
    FeedService feedService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> saveFeed(
            @RequestParam("textContent") String textContent,
            @RequestParam("location") String location,
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude,
            @RequestParam(value = "imageContent", required = false) MultipartFile[] imageContent
    ) {
        try {
//            log.info("params: {}", allParams);
            Feed feed = new Feed(textContent, location, longitude, latitude, imageContent);
            log.info("params: {}", feed);
            Boolean aBoolean = feedService.saveFeed(feed);
            if(aBoolean) {
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            log.error("save feed error: {}", e.toString());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping()
    public PageResult queryPages(@RequestParam int page, @RequestParam int pageSize) {
        try {
            User user = UserThreadLocal.get();
            return feedService.queryPages(user.getId(), page, pageSize);
        } catch (Exception e) {
            log.error("get timeline feeds error: {}", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("comment")
    public ResponseEntity<Void> addComment(@RequestBody Comment comment) {
        try {
            feedService.addComment(comment);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("add comment error {}", e.toString());
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("comment/cont")
    public ResponseEntity<Long> getCommentCount(int commentType, String publishId) {
        try {
            Comment comment = new Comment();
            comment.setCommentType(commentType);
            comment.setPublishId(new ObjectId(publishId));
            Long commentCount = feedService.getCommentCount(comment);
            return ResponseEntity.ok().body(commentCount);
        } catch (Exception e) {
            log.error("add comment error {}", e.toString());
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
