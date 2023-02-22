package org.cc.server.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class RedisCacheInterceptor implements HandlerInterceptor {
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${cc.cache.enable}")
    private Boolean enable;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("enter interceptor");
        if (!enable) {
            log.info("disable interceptor");

            //未开启缓存
            return true;
        }

        String method = request.getMethod();
        if (!StringUtils.equalsAnyIgnoreCase(method, "GET")) {
            log.info("disable cache with none get: {}", method);

            // 非GET的请求不进行缓存处理
            return true;
        }

        // 通过缓存做命中，查询redis，redisKey ?  组成：md5（请求的url + 请求参数）
        String redisKey = createRedisKey(request);
        log.info("get cache from redis with key: {}", redisKey);
        String data = this.redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(data)) {
            log.info("get no cache");
            // 缓存未命中
            return true;
        }
        log.info("get data from redis cache: {}", data);

        // 将data数据进行响应
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(data);

        return false;
    }

    public static String createRedisKey(HttpServletRequest request) throws
            Exception {
        String paramStr = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.isEmpty()) {
            //请求体的数据只能读取一次，需要进行包装Request进行解决
            paramStr += IOUtils.toString(request.getInputStream(), "UTF-8");
        } else {
            paramStr += mapper.writeValueAsString(request.getParameterMap());
        }

        String authorization = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty((authorization))) {
            paramStr += "_" + authorization;
        }

        return "SERVER_DATA_" + DigestUtils.md5Hex(paramStr);
    }
}
