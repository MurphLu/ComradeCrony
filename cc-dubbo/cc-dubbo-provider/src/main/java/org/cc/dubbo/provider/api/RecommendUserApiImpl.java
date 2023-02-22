package org.cc.dubbo.provider.api;

import org.apache.dubbo.config.annotation.DubboService;
import org.cc.dubbo.api.RecommendUserApi;
import org.cc.dubbo.pojo.RecommendUser;
import org.cc.dubbo.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@DubboService
public class RecommendUserApiImpl implements RecommendUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RecommendUser queryWithMaxScore(Long userId) {
        Criteria criteria = Criteria.where("toUserId").is(userId);
        Query query = Query.query(criteria).with(Sort.by("score").descending()).limit(1);
        return mongoTemplate.findOne(query, RecommendUser.class);

    }

    @Override
    public PageInfo<RecommendUser> queryPageInfo(Long userId, Integer pageNum, Integer pageSize) {
        Criteria criteria = Criteria.where("toUserId").is(userId);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNum - 1);
        Query query = Query.query(criteria).with(pageable);
        List<RecommendUser> recommendUsers = mongoTemplate.find(null, RecommendUser.class);
        return new PageInfo<>(null, pageNum, pageSize, recommendUsers);
    }

    @Override
    public double queryScore(Long userId, Long toUserId) {
        Criteria criteria = Criteria.where("toUserId").is(toUserId).and("userId").is(userId);
        Query query = Query.query(criteria);
        RecommendUser user = mongoTemplate.findOne(query, RecommendUser.class);
        if(user == null) {
            return 0;
        }
        return user.getScore();
    }
}
