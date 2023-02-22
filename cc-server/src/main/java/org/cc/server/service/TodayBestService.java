package org.cc.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.cc.dubbo.pojo.RecommendUser;
import org.cc.dubbo.vo.PageInfo;
import org.cc.server.pojo.User;
import org.cc.server.pojo.UserInfo;
import org.cc.server.vo.PageResult;
import org.cc.server.vo.RecommendUserQueryParam;
import org.cc.server.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public PageResult queryRecommendPageInfo(String token, RecommendUserQueryParam param) {
        User user = this.userService.getUserByToken(token);
        if(user == null) {
            return null;
        }
        PageInfo<RecommendUser> userPageInfo = recommendUserService
                .queryRecommendPageInfo(user.getId(), param);
        List<RecommendUser> userList = userPageInfo.getRecords();
        List<Long> ids = userList
                .stream()
                .map(RecommendUser::getUserId)
                .collect(Collectors.toList());
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", ids);
        List<UserInfo> list = userInfoService.list(queryWrapper);
        List<TodayBest> todayBestList = list
                .stream()
                .map(
                        userInfo -> {
                            TodayBest todayBest = parseUserInfo(userInfo);
                            RecommendUser first = userList.stream()
                                    .filter(item -> Objects.equals(item.getUserId(), userInfo.getUserId()))
                                    .findFirst()
                                    .get();
                            double score = Math.floor(first.getScore());
                            todayBest.setFateValue(Double.valueOf(score).longValue());
                            return todayBest;
                        })
                .sorted(
                        (o1, o2) -> Long
                                .valueOf(o2.getFateValue() - o1.getFateValue())
                                .intValue()
                ).collect(Collectors.toList());
        return new PageResult(0, param.getPagesize(), 0, param.getPage(), todayBestList);
    }

    private TodayBest parseUserInfo(UserInfo userInfo) {
        TodayBest todayBest = new TodayBest();
        todayBest.setAvatar(userInfo.getLogo());
        todayBest.setNickname(userInfo.getNickName());
        todayBest.setAge(userInfo.getAge());
        todayBest.setGender(userInfo.getSex().getDesc());
        todayBest.setTags(userInfo.getTags().split(","));
        todayBest.setId(userInfo.getUserId());
        return todayBest;
    }
}
