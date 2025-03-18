package com.itshixun.industy.fundusexamination.Service;

import com.itshixun.industy.fundusexamination.pojo.dto.DiagnosedCaseDto;
import org.springframework.stereotype.Service;


public interface DiagService {
    /**
     *  获取诊断信息
     * @param caseId
     * @return
     */
    DiagnosedCaseDto getDiag(String caseId);
    /**
     * 添加诊断信息
     * @param diag
     * @return
     */
    DiagnosedCaseDto addDiag(DiagnosedCaseDto diag);
    /**
     * 更新诊断信息
     * @param diag
     * @return
     */
    DiagnosedCaseDto updateDiag(DiagnosedCaseDto diag);

    /**
     *  删除诊断信息
     * @param id
     */
    void deleteDiag(String id);
}
