// CaseDto.java
package com.itshixun.industy.fundusexamination.pojo.dto;


import com.itshixun.industy.fundusexamination.pojo.*;
import jakarta.persistence.Column;
import lombok.Data;

/**
 * @author 10169
 * @Description 导入病例模块
 * @Date 2023/4/18 15:40
 *
 **/
@Data
public class CaseDto {
    // 病例信息
    private Integer id;
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
    private PreImageData preImageData;
    //疾病概率
    private DiseaseRate diseaseRate;


}