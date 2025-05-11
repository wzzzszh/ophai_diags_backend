package com.itshixun.industy.fundusexamination.pojo.dto;

import com.itshixun.industy.fundusexamination.pojo.Mark;
import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 孙宗昊
 * @Description 更新病例DTO
 * 用于接收前端传来的病例信息
 * @Date 2023/4/18 15:40
 *
 **/

@Data
public class CaseUpdateDTO {

    // 病例信息
    private String caseId;

    //疾病类型
    private Integer diseaseType;

    //疾病name
    private String[] diseaseName;

    //诊断状态
    private Integer diagStatus;

    //患者表
    private PatientInfo patientInfo;

    //医生诊断
    private NormalDiagDto normalDiag;



    // 批注列表
    private List<Mark> marks;

    //创建时间
    private LocalDateTime createDate;

    //更新时间
    private LocalDateTime updateDate;
}
