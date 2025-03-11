// AiDiagDto.java
package com.itshixun.industy.fundusexamination.pojo.dto;


import com.itshixun.industy.fundusexamination.pojo.DiseaseRate;
import lombok.Data;

import java.util.List;
/**
 * @author 10169
 * @Description Ai诊断结果模块
 * @Date 2023/4/18 15:40
 *
 **/
@Data
public class AiDiagDto {
    private Long id;
    //疾病类型
    private Integer diseaseType;
    //AI诊断结果列表1
    private String aiDiagnosis1;
    //AI诊断结果列表2
    private String aiDiagnosis2;
    //AI诊断结果列表3
    private String aiDiagnosis3;
    //Ai建议措施1
    private String aiSuggestion1;
    //Ai建议措施2
    private String aiSuggestion2;
    //Ai建议措施3
    private String aiSuggestion3;

    //疾病概率
    private DiseaseRate diseaseRate;

}