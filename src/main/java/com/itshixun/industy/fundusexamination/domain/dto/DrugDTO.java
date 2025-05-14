package com.itshixun.industy.fundusexamination.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugDTO {
    private String function;
    private List<String> drug;
}