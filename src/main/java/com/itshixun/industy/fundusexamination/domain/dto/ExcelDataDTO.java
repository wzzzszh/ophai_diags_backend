package com.itshixun.industy.fundusexamination.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelDataDTO {
    // 基础字段
    @ExcelProperty("病例ID")
    private String caseId;

    // AI信息解析字段
    @ExcelProperty("预测结果")
    private String predictions;

    @ExcelProperty("复查建议")
    private String suggestions;

    @ExcelProperty("复查时间")
    private String revisitTime;

    @ExcelProperty("推荐药品")
    private String drugs;

    @ExcelProperty("报告链接")
    private String reportHtml;

    @ExcelProperty("二维码链接")
    private String qrCode;
}