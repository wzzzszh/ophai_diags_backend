// DiagnosedCaseDto.java
package com.itshixun.industy.fundusexamination.pojo.dto;

import com.itshixun.industy.fundusexamination.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 10169
 * @Description AAA综合诊断界面
 * @Date 2023/4/18 15:40
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosedCaseDto {
    //CaseDto
    // 病例信息
    private String id;
    // 疾病类型
    private Integer diseaseType;
    // 责任医师
    private String responsibleDoctor;
    // 诊断状态
    private Integer diagStatus;
    // 原始图片
    private OriginImageData originImageData;
    // 处理后的图片
    private PreImageData preImageData;
    // 疾病概率
    private DiseaseRate diseaseRate;
    // 患者表
    private PatientInfo patientInfo;
    //HistoryCase
    //病例库(查询实现)
    private List<HistoryCase> historyCases;
    // AI诊断结果
    private AiDiag aiDiag;
    // 普通诊断结果
    private NormalDiag normalDiag;

}