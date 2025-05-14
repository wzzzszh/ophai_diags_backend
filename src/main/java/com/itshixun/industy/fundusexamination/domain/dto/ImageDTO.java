package com.itshixun.industy.fundusexamination.domain.dto;

import com.itshixun.industy.fundusexamination.domain.po.OriginImageData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    private String caseId;

    private OriginImageData originImageData;

    private String aiCaseInfo;

}
