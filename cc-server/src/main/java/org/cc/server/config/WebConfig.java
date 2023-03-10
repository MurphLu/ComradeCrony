package org.cc.server.config;

import org.cc.server.interceptor.CorsInterceptor;
import org.cc.server.interceptor.RedisCacheInterceptor;
import org.cc.server.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    RedisCacheInterceptor redisCacheInterceptor;

    @Autowired
    TokenInterceptor tokenInterceptor;

    @Autowired
    CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
        registry.addInterceptor(redisCacheInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/feed/comment/cont");
    }

    //
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(10000);
    }
}
