package com.dj.p2p.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/user/**")
                .excludePathPatterns("/user/login","/swagger-resources/**","/swagger-ui.html/**","/v2/**")
                .excludePathPatterns("/user/add","/userApi/**","/risk/login");

        registry.addInterceptor(getRiskMyInterceptor()).addPathPatterns("/risk/**")
                .excludePathPatterns("/swagger-resources/**","/swagger-ui.html/**","/v2/**")
                .excludePathPatterns("/user/add","/userApi/**","/risk/login");
        super.addInterceptors(registry);
    }
    @Bean
    public HandlerInterceptor getMyInterceptor(){
        return new UserInterceptor();
    }

    @Bean
    public HandlerInterceptor getRiskMyInterceptor(){
        return new RiskInterceptor();
    }
}
