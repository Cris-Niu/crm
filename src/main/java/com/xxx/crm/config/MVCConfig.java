package com.xxx.crm.config;

import com.xxx.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置登录拦截器
 */
@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return  new NoLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(noLoginInterceptor()).addPathPatterns("/**")
                //需要放行的资源
                .excludePathPatterns("/css/**","/images/**","/js/**","/lib/**","/index","/user/login");
    }
}
