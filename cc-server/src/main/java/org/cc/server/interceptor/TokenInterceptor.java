package org.cc.server.interceptor;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.cc.server.annutation.NoNeedAuth;
import org.cc.server.pojo.User;
import org.cc.server.service.UserService;
import org.cc.server.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
