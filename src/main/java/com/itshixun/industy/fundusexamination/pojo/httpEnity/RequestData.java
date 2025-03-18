package com.itshixun.industy.fundusexamination.pojo.httpEnity;// com.itshixun.industy.test.http.RequestData.java（发送端和接收端共用）
import lombok.Data;

@Data
public class RequestData {
    private String content; // 必须字段
    private String timestamp; // 可选字段
}