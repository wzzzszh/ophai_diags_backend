package com.itshixun.industy.fundusexamination.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginUserDTO {

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
