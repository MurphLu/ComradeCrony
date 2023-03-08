package org.cc.server.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CorsInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setHeader("Access-Control-Allow-Origin", "*");//允许任何请求来源
            response.setHeader("Access-Control-Allow-Methods", "*");//允许任何method
            response.setHeader("Access-Control-Allow-Headers", "*");//允许任何自定义header
            response.setStatus(HttpStatus.OK.value());
            return false;
        }
        return true;
    }
}
