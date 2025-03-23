package com.itshixun.industy.fundusexamination.pojo.dto;

import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import lombok.Data;

import java.time.LocalDateTime;
@Data
//病例库(dto)
public class historyCaseListDto {
    //病例ID
    private String caseId;
    //疾病类型
    private Integer diseaseType;
    //诊断状态
    private Integer diagStatus;
    //创建时间
    private LocalDateTime createDate;
    //更新时间
    private LocalDateTime updateDate;
}
