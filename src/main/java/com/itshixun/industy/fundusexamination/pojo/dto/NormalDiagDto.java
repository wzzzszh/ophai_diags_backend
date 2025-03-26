package com.itshixun.industy.fundusexamination.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NormalDiagDto {
    private String nDiagId;

    private String doctorName;

    private String docSuggestions;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;
}
