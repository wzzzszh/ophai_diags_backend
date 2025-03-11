package com.itshixun.industy.fundusexamination.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AlgorithmClient {
    @Value("${algorithm.endpoint}")
    private String algorithmEndpoint;

    @Value("${algorithm.api-path}")
    private String apiPath;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendToAlgorithm(String data) {
        //python端的url
        String url = algorithmEndpoint + apiPath;

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 封装请求体
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        // 发送POST请求
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("算法服务调用失败，状态码：" + response.getStatusCode());
    }
}