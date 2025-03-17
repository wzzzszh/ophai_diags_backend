package com.itshixun.industy.fundusexamination.Service;

import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;

public interface PreImageService {
    /**
     * 保存并诊断
     * @param caseDto
     * @return
     */
    Case saveAndDiag(CaseDto caseDto);
}
