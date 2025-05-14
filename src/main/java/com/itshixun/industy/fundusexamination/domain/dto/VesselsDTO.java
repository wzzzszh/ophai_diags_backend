package com.itshixun.industy.fundusexamination.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VesselsDTO {
    private String left;
    private String right;
}
