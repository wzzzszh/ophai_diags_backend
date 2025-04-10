package com.itshixun.industy.fundusexamination.pojo.dto;


import com.fasterxml.jackson.databind.JsonNode;
import com.itshixun.industy.fundusexamination.pojo.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * 查询单个病例Dto
 *
 * @author 10169
 */
@Data

public class JcaseDto {
    // 病例信息
    private String caseId;
    //jsonAi诊断
    private JsonNode aiCaseInfoJson;
    //疾病name
    private String[] diseaseName;
    //历史病例库
    private PageBean<historyCaseListDto> historyCaseListDto;
    //医生诊断
    private List<NormalDiagDto> doctorDiags;
    //疾病类型
    private Integer diseaseType;
    //责任医师
    private String responsibleDoctor;
    //诊断状态
    private Integer diagStatus;
    //患者表
    private PatientInfo patientInfo;
    //原始图片
    private OriginImageData originImageData;
    //创建时间
    private LocalDateTime createDate;
    //更新时间
    private LocalDateTime updateDate;
}
