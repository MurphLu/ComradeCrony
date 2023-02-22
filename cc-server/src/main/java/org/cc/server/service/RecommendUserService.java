package org.cc.server.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cc.dubbo.api.RecommendUserApi;
import org.cc.dubbo.pojo.RecommendUser;
import org.cc.dubbo.vo.PageInfo;
import org.cc.server.vo.RecommendUserQueryParam;
import org.cc.server.vo.TodayBest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RecommendUserService {
    @DubboReference
    private RecommendUserApi recommendUserApi;

    public TodayBest queryTodayBest(Long userId) {
        TodayBest todayBest = null;
        try {
            RecommendUser recommendUser = recommendUserApi.queryWithMaxScore(userId);
            log.info("get recommendUser success: {}", recommendUser);
            double score = Math.floor(recommendUser.getScore());
            todayBest = new TodayBest();
            todayBest.setFateValue(Double.valueOf(score).longValue());
            todayBest.setId(recommendUser.getUserId());
        } catch (Exception e) {
            log.error("get recommendUser error: {}", e.toString());
        }
        return todayBest;
    }

    public PageInfo<RecommendUser> queryRecommendPageInfo(Long id, RecommendUserQueryParam param) {
        PageInfo<RecommendUser> recommendUserPageInfo = null;
        try {
            recommendUserPageInfo = recommendUserApi.queryPageInfo(id, param.getPage(), param.getPagesize());
            log.info("get recommendUserPageInfo success: {}", recommendUserPageInfo);
        } catch (Exception e) {
            log.error("get recommendUserPageInfo error: {}", e.toString());
        }
        return recommendUserPageInfo;
    }
}
