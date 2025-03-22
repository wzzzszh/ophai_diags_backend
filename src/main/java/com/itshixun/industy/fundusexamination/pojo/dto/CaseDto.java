// CaseDto.java
package com.itshixun.industy.fundusexamination.pojo.dto;


import com.itshixun.industy.fundusexamination.pojo.*;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 10169
 * @Description 导入病例模块
 * @Date 2023/4/18 15:40
 *
 **/
@Data
public class CaseDto {
    // 病例信息
    private String caseId;
    //ai诊断
    private String aiCaseInfo;
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
    //处理后的图片
    @Nullable
    private PreImageData preImageData;
    //疾病概率
    private DiseaseRate diseaseRate;
    //删除字段
    private Integer isDeleted;
    //创建时间
    private LocalDateTime createDate;
    //更新时间
    private LocalDateTime updateDate;

}