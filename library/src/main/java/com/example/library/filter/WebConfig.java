package com.example.library.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessControlInterceptor()).excludePathPatterns("/user/login").excludePathPatterns("/forget/**").excludePathPatterns("/user/register"); // 排除不需要拦截的路径

    }
}
