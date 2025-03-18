package com.itshixun.industy.fundusexamination.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册Hibernate模块，启用对懒加载代理对象的支持
        Hibernate6Module hibernateModule = new Hibernate6Module();
        // 可选配置：忽略未初始化的属性（避免触发数据库查询）
        hibernateModule.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
        objectMapper.registerModule(hibernateModule);
        // 2. 处理Java 8日期时间类型（新增配置）
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}