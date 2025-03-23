// CaseLibDto.java
package com.itshixun.industy.fundusexamination.pojo.dto;


import com.fasterxml.jackson.databind.JsonNode;
import com.itshixun.industy.fundusexamination.pojo.DiseaseRate;
import com.itshixun.industy.fundusexamination.pojo.OriginImageData;
import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import com.itshixun.industy.fundusexamination.pojo.PreImageData;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @author 10169
 * @Description 分页查询的DTO
 * @Date
 *
 **/
@Data
public class CaseLibDto {
    // 病例信息
    private String caseId;
    //疾病类型
    private Integer diseaseType;
    //责任医师
    private String responsibleDoctor;
    //诊断状态
    private Integer diagStatus;
    //患者表
    private PatientInfo patientInfo;
    //创建时间
    private LocalDateTime createDate;
    //更新时间
    private LocalDateTime updateDate;


}