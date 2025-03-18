package com.itshixun.industy.fundusexamination.pojo.httpEnity;// src/main/java/com/example/demo/config/com.itshixun.industy.test.http.RestTemplateConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 基础配置（可选）
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}