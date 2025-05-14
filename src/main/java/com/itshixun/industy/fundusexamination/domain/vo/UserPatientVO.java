package com.itshixun.industy.fundusexamination.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPatientVO {
    // 用户ID
    private String userId;

    private String idCard;
    // 手机号
    private String phone;
    // 真实姓名
    private String name;
    // 性别
    // 0: 男
    // 1: 女
    private int gender;
    // 年龄
    private int age;

    private String address;

    private String medicalCard;

    private String emergencyContact;

}
