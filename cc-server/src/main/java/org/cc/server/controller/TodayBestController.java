package org.cc.server.controller;

import org.cc.server.service.TodayBestService;
import org.cc.server.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cc")
public class TodayBestController {

    @Autowired
    TodayBestService service;

    @GetMapping("todayBest")
    public TodayBest queryTodayBest(@RequestHeader("Authorization") String token) {
        return service.queryTodayBest(token);
    }
}
