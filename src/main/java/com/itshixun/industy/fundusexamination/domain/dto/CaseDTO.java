// CaseDto.java
package com.itshixun.industy.fundusexamination.domain.dto;


import com.itshixun.industy.fundusexamination.domain.po.OriginImageData;
import com.itshixun.industy.fundusexamination.domain.po.PatientInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 10169
 * @Description 导入病例模块
 * @Date 2023/4/18 15:40
 *
 **/
@Data
public class CaseDTO {
    // 病例信息
    private String caseId;
    //ai诊断
    private String aiCaseInfo;
    //疾病类型
    private Integer diseaseType;
    //责任医师
    private String responsibleDoctor;
    //疾病name
    private String[] diseaseName;
    //诊断状态
    private Integer diagStatus;
    //患者表
    private PatientInfo patientInfo;
    //医生诊断
    private NormalDiagDTO normalDiag;
    //医生诊断
    private List<NormalDiagDTO> normalDiags;  // 复数形式
    //原始图片
    private OriginImageData originImageData;
    //删除字段
    private Integer isDeleted;
    //创建时间
    private LocalDateTime createDate;
    //更新时间
    private LocalDateTime updateDate;

}