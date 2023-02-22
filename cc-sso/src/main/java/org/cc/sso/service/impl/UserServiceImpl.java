package org.cc.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cc.sso.mapper.UserMapper;
import org.cc.sso.pojo.User;
import org.cc.sso.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static String secret = "comrade_crony";
    private static final ObjectMapper MAPPER = new ObjectMapper();


    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public String login(String mobile, String code) {
        String redisKey = "SMS_CODE_" + mobile;
        String value = this.redisTemplate.opsForValue().get(redisKey);
        if(StringUtils.isEmpty(value)) {
            return null;
        }
        if(!StringUtils.equals(value, code)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        User user = userMapper.selectOne(queryWrapper);
        boolean isNew = false;
        if(null == user){
            isNew = true;
            user = new User();
            user.setMobile(mobile);
            user.setPassword(DigestUtils.md5DigestAsHex("12345".getBytes()));
            this.userMapper.insert(user);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("mobile", mobile);
        claims.put("id", user.getId());
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secret).compact();
        String redisTokenKey = "TOKEN_" + token;
        String serilUser = null;
        try {
            serilUser = MAPPER.writeValueAsString(user);
            redisTemplate.opsForValue().set(redisTokenKey, serilUser, Duration.ofHours(1));
        } catch (JsonProcessingException e) {
            LOGGER.error("保存 token 失败", e);
            return null;
        }
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("id", user.getId());
            msg.put("mobile", mobile);
            msg.put("date", new Date());
            this.rocketMQTemplate.convertAndSend("comrade_crony-sso-login", msg);
        } catch (Exception e) {
            LOGGER.error("send to rocket mq error:", e);
        }
        return isNew + "|" + token;
    }

    @Override
    public User queryUserByToken(String token) {
        try{
            String redisTokenKey = "TOKEN_" + token;
            String userValue = redisTemplate.opsForValue().get(redisTokenKey);
            if(StringUtils.isEmpty(userValue)) {
                return null;
            }
            redisTemplate.opsForValue().set(redisTokenKey, userValue, Duration.ofHours(1));
            return MAPPER.readValue(userValue, User.class);
        }catch (Exception e) {
            LOGGER.error("get cached use error: ", e);
            return null;
        }
    }
}
