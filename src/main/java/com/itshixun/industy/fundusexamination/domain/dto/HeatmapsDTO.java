package com.itshixun.industy.fundusexamination.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeatmapsDTO {
    private List<String> contains;
    @JsonProperty("left")
    private Map<String, List<String>> leftDiseases;
    @JsonProperty("right")
    private Map<String, List<String>> rightDiseases;
}
