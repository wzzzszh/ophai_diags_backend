package com.itshixun.industy.fundusexamination.pojo.Config;

import com.itshixun.industy.fundusexamination.Utils.AliOssUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ali.oss")
public class AliOssConfig {

    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;

    // Getter 和 Setter
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    @Bean
    public AliOssUtil aliOssUtil() {
        return new AliOssUtil(this.endpoint, this.accessKeyId, this.secretAccessKey);
    }
}