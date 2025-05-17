package com.itshixun.industy.fundusexamination.domain.vo;

import com.itshixun.industy.fundusexamination.domain.po.PatientInfo;
import lombok.Data;

/**
 * 用户DTO
 */
@Data
public class UserVO {
    // 用户ID
    private String userId;

    private String userName;

    private String idNumber;
    // 邮箱
    private String email;
    // 手机号

    private String phone;
    // 真实姓名
    private String realName;

    private Integer doctorNumber;
    // 性别
    // 0: 男
    // 1: 女
    private int gender;
    // 年龄
    private int age;
    // 所在医院
    private String hospital;
    // 职位
    private String position;
    //token
    private String token;

    private String invitationCode;

    private int permission;

    private PatientInfo patientInfo;

}