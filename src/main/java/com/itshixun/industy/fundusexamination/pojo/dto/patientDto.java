package com.itshixun.industy.fundusexamination.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class patientDto {

    private String patientId;

    private String name;

    private Integer age;

    private Integer gender;

    private LocalDateTime createDate;
}
