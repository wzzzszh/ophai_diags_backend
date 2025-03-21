package com.itshixun.industy.fundusexamination.pojo.httpEnity;// com.itshixun.industy.test.http.ResponseData.java（接收端返回）
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    private String message;   // 业务响应内容
    private Boolean success;       // HTTP状态码（例如200/400）
}