package org.cc.server.service;

import lombok.extern.slf4j.Slf4j;
import org.cc.server.pojo.User;
import org.cc.server.pojo.UserInfo;
import org.cc.server.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TodayBestService {

    @Autowired
    UserService userService;

    @Autowired
    RecommendUserService recommendUserService;

    @Autowired
    UserInfoService userInfoService;

    public TodayBest queryTodayBest(String token){
        User user = userService.getUserByToken(token);
        log.info("get userinfo: {}", user);
        TodayBest todayBest = recommendUserService.queryTodayBest(user.getId());
        log.info("get todayBest success: {}", todayBest);
        UserInfo userInfo = userInfoService.queryUserInfoById(todayBest.getId());
        log.info("get userInfo success: {}", userInfo);
        todayBest.setAvatar(userInfo.getLogo());
        todayBest.setNickname(userInfo.getNickName());
        todayBest.setAge(userInfo.getAge());
        todayBest.setGender(userInfo.getSex().getDesc());
        todayBest.setTags(userInfo.getTags().split(","));
        log.info("update today best success: {}", todayBest);

        return todayBest;
    }
}
