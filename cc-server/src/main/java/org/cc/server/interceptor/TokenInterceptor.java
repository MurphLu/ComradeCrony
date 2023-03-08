package org.cc.server.interceptor;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.cc.server.annutation.NoNeedAuth;
import org.cc.server.pojo.User;
import org.cc.server.service.UserService;
import org.cc.server.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            return false;
        }
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NoNeedAuth annotation = handlerMethod.getMethod().getAnnotation(NoNeedAuth.class);
            if(annotation!=null) {
                return true;
            }
        }
        String token = request.getHeader("Authorization");
        User user = userService.getUserByToken(token);
        if(null == user) {
            response.setStatus(HttpResponseStatus.UNAUTHORIZED.code());
            return false;
        }
        UserThreadLocal.set(user);
        return true;
    }
}
