package org.cc.server.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
//@WebFilter(urlPatterns = "/**", filterName = "CorsFilter")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");//允许任何请求来源
        httpResponse.setHeader("Access-Control-Allow-Methods", "*");//允许任何method
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");//允许任何自定义header
        if (!httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())){
            chain.doFilter(request, response);
        }
    }
}
