package com.itshixun.industy.fundusexamination.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NormalDiagVO {
    private String nDiagId;

    private String doctorName;

    private String docSuggestions;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;
}
