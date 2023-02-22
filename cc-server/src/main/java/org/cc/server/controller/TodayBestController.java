package org.cc.server.controller;

import org.cc.dubbo.pojo.RecommendUser;
import org.cc.dubbo.vo.PageInfo;
import org.cc.server.service.TodayBestService;
import org.cc.server.vo.PageResult;
import org.cc.server.vo.RecommendUserQueryParam;
import org.cc.server.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cc")
public class TodayBestController {

    @Autowired
    TodayBestService service;

    @GetMapping("todayBest")
    public TodayBest queryTodayBest(@RequestHeader("Authorization") String token) {
        return service.queryTodayBest(token);
    }

    @GetMapping("recommendList")
    public PageResult queryRecommendList(@RequestHeader("Authorization") String token, RecommendUserQueryParam param) {
        return service.queryRecommendPageInfo(token, param);
    }
}
