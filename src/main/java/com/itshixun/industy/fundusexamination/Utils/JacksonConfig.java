package com.itshixun.industy.fundusexamination.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper httpObjectMapper = new ObjectMapper();
        // 注册模块
        httpObjectMapper.registerModule(new JavaTimeModule());
        httpObjectMapper.registerModule(new Hibernate6Module());
        // 禁用日期时间戳格式，启用友好格式
        httpObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return httpObjectMapper;
    }

    // 可选：确保 Spring MVC 使用此 ObjectMapper
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder(ObjectMapper objectMapper) {
        return new Jackson2ObjectMapperBuilder()
                .modules(new JavaTimeModule(), new Hibernate6Module())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}