package com.itshixun.industy.fundusexamination.Utils;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 生产环境建议替换为具体域名
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(MAX_AGE);

        // 公开接口路径
        source.registerCorsConfiguration("/api/**", config);
//        // 内部接口路径（需要身份验证）
//        CorsConfiguration internalConfig = new CorsConfiguration();
//        internalConfig.addAllowedOrigin("https://admin.your-frontend.com");
//        internalConfig.setAllowedMethods(Arrays.asList("GET", "POST"));
//        source.registerCorsConfiguration("/api/internal/**", internalConfig);

        return new CorsFilter(source);
    }
}