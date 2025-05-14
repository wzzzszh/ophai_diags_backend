package com.itshixun.industy.fundusexamination.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AICaseInfoDTO {
    private PredictionsDTO predictions;
    private ImagesDTO images;
    private List<String> suggestions;
    private String revisit_time;
    private List<DrugDTO> drugs;
    private String report_html;
    private String qr_code;
}

