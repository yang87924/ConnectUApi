package com.connectu.connectuapi.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyGlobalCORSConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许跨域访问的路径
                        .allowedOrigins("http://localhost:6999",
                                "https://apifox.com/apidoc/shared-c32e3a6f-756f-43e3-aed6-5408aa8d0247/api-88082215",
                                "http://43.207.68.213:6999",
                                "http://43.207.68.213",
                                "http://connectu.life",
                                "https://connectu.life",
                                "http://forum.connectu.life",
                                "https://forum.connectu.life") // 允许跨域访问的源
          //             .allowedOrigins("*")
                        .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // 允许请求的方法
                        .maxAge(168000) // 预检间隔时间
                        .allowedHeaders("*") // 允许头部设置
                        .allowCredentials(true); // 是否发送cookie
            }
        };
    }

}