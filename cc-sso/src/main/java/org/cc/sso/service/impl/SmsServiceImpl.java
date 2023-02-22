package org.cc.sso.service.impl;

import org.cc.sso.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public Map<String, Object> sendSms(String mobile) {
        String redisKey = "SMS_CODE_" + mobile;

        String value = this.redisTemplate.opsForValue().get(redisKey);

        Map<String, Object> result = new HashMap<>();

        if(StringUtils.isEmpty(value)) {
//            int smsCode = RandomUtils.nextInt(100000, 999999);
            int smsCode = 123456;
            // 调用短信服务发送短信
            result.put("code", 0);
            result.put("data", smsCode);

            redisTemplate.opsForValue().set(redisKey, String.valueOf(smsCode), 5, TimeUnit.MINUTES);

        } else {
            result.put("code", 1);
            result.put("data", value);
            result.put("msg", "请使用上一次的验证码");
//            Result result = Result.builder().code("1").msg("请使用上一次验证码").data(value).build();
        }
        log.info("SMS_CODE: {}", result);
        return result;
    }

    public Map<String, Object> checkSms(String mobile, String smsCode) {
        String redisKey = "SMS_CODE_" + mobile;
        String value = this.redisTemplate.opsForValue().get(redisKey);
        Map<String, Object> result = new HashMap<>();
        if(smsCode.equals(value)) {
            result.put("code", 0);
            result.put("msg", "验证成功");
        } else {
            result.put("code", 1);
            result.put("msg", "验证码错误");
        }
        return result;
    }
}
