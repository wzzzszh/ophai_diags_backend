package com.itshixun.industy.fundusexamination.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class LoginUserDto {

    private String userId;

    private String userName;

    private String hospital;

    private String idNumber;

    private String email;

    private String phone;

    private int gender;

    private int age;

    private String token;

    private LocalDateTime createDate;
}
