package com.connectu.connectuapi.config;

import com.connectu.connectuapi.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor()).addPathPatterns("")
                .excludePathPatterns("/users/login","/users/google","/users/getUserId")
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html");



    }
}
