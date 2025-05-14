package com.itshixun.industy.fundusexamination.service;

import com.itshixun.industy.fundusexamination.domain.dto.*;
import com.itshixun.industy.fundusexamination.domain.po.Case;
import com.itshixun.industy.fundusexamination.domain.po.Mark;
import com.itshixun.industy.fundusexamination.domain.po.PageBean;

import java.util.List;

public interface CaseService {
    /**
     * 添加病例数据
     * @param caseDto
     * @return
     */
    Case add(CaseDTO caseDto);

    /**
     * //条件分页查询
     * @param pageNum
     * @param pageSize
     * @param diagStatus
     * @param diseaseName
     * @param patientInfoPatientId
     * @return
     */
    PageBean<CaseLibDTO> getCaseListByPage(Integer pageNum,
                                           Integer pageSize,
                                           Integer diagStatus,
                                           String[] diseaseName,
                                           String patientInfoPatientId);

    /**
     * 更新病例数据
     * @param caseDto
     * @return
     */
    CaseDTO update(CaseDTO caseDto);

    /**
     * 逻辑删除病例数据
     * @param caseId
     */
    void delete(String caseId);
    /**
     * 根据id查询患者
     * @param patientId
     * @return
     */
    boolean isPatientExist(String patientId);

    Case getCaseById(String caseId);

    CaseUpdateDTO updateNorDiag(String caseId,CaseUpdateDTO caseDto);

    PageBean<HistoryCaseListDTO> getHistoryCaseListByPage(String patientId);

    List<Object[]> getNormalDiagByCaseId(String caseId);

    List<Mark> getMarksByCaseId(String caseId);

    JCaseDTO getRealCaseById(String caseId);
}
