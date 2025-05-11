package com.itshixun.industy.fundusexamination.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class patientLibDto {

    private String patientId;

    private String name;

    private Integer age;

    private Integer gender;

    private String phone;

    private String idCard;

    private String address;

    private String medicalCard;

    private String emergencyContact;

    private LocalDateTime createDate;
}
