package com.itshixun.industy.fundusexamination.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionsDTO {
    private Double D;
    private Double G;
    private Double C;
    private Double A;
    private Double H;
    private Double M;
    private Double O;
}