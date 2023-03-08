package org.cc.server.controller;

import org.cc.dubbo.pojo.RecommendUser;
import org.cc.dubbo.vo.PageInfo;
import org.cc.server.service.TodayBestService;
import org.cc.server.vo.PageResult;
import org.cc.server.vo.RecommendUserQueryParam;
import org.cc.server.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("cc")
public class TodayBestController{

    @Autowired
    TodayBestService service;

    @GetMapping("todayBest")
    public TodayBest queryTodayBest() {
        return service.queryTodayBest();
    }

    @GetMapping("recommendList")
    public PageResult queryRecommendList(RecommendUserQueryParam param) {
        return service.queryRecommendPageInfo(param);
    }
}
