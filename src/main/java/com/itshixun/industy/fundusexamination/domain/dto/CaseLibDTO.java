// CaseLibDto.java
package com.itshixun.industy.fundusexamination.domain.dto;


import com.itshixun.industy.fundusexamination.domain.po.PatientInfo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 10169
 * @Description 分页查询的DTO
 * @Date
 *
 **/
@Data
public class CaseLibDTO {
    // 病例信息
    private String caseId;
    //病例名称
    private String[] diseaseNameJ;
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