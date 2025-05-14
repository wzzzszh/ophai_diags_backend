package com.itshixun.industy.fundusexamination.domain.httpEnity;

import lombok.Data;

import java.util.List;

@Data
public  class DrugRecommendation {
    private String function;
    private List<String> drag;
}
