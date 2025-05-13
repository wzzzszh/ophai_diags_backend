package com.itshixun.industy.fundusexamination;

import com.itshixun.industy.fundusexamination.pojo.Config.AliOssConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AliOssConfig.class)
public class FundusExaminationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundusExaminationApplication.class, args);
    }

}
