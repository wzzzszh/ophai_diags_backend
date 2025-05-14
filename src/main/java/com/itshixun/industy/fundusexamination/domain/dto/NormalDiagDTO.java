package com.itshixun.industy.fundusexamination.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NormalDiagDTO {
    private String nDiagId;

    private String doctorName;

    private String docSuggestions;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;
}
