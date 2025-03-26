package com.itshixun.industy.fundusexamination.Utils;

import com.itshixun.industy.fundusexamination.Utils.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登陆注册接口不拦截
        registry.addInterceptor(loginInterceptor).
                excludePathPatterns("/api/user/login","/api/user/logout","/api/user/register");
    }
}
