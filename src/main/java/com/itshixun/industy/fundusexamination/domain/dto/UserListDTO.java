package com.itshixun.industy.fundusexamination.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDTO {
    private String userId;

    // 用户名
    private String userName;

    //身份证号
    private String idNumber;
    //医师证号码
    private Integer doctorNumber;
    // 邮箱
    private String email;
    // 手机号
    private String phone;
    // 真实姓名
    private String realName;
    // 性别
    // 0: 女
    // 1: 男
    private int gender;
    // 年龄
    private int age;
    // 所在医院
    private String hospital;
    // 职位
    private String position;
    //头像url
    private String avatarUrl;
    //创建时间
    private LocalDateTime createDate;

    //修改时间
    private Date updateDate;

    //用户权限
    private int permission;

}
