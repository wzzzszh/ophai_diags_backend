package com.itshixun.industy.fundusexamination.Utils.RabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ImageProcessMessage implements Serializable {
    private static final long serialVersionUID = 1L;// 添加序列化版本号
    private String caseId;
    private String leftImageUrl;
    private String rightImageUrl;
    private String patientName;
    private int patientAge;
    private int patientGender;
}