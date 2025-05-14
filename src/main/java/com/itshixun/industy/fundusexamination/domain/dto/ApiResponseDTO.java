package com.itshixun.industy.fundusexamination.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiResponseDTO {
    private boolean success;
    @JsonProperty("message") // 映射 JSON 中的 "message" 字段
    private AICaseInfoDTO aiCaseInfo;
}