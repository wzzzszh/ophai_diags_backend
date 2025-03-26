package com.itshixun.industy.fundusexamination.Service;

import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.NormalDiag;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.User;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseLibDto;
import com.itshixun.industy.fundusexamination.pojo.dto.historyCaseListDto;

import java.util.List;

public interface CaseService {
    /**
     * 添加病例数据
     * @param caseDto
     * @return
     */
    Case add(CaseDto caseDto);

    /**
     * //条件分页查询
     * @param pageNum
     * @param pageSize
     * @param diagStatus
     * @param diseaseName
     * @param patientInfoPatientId
     * @return
     */
    PageBean<CaseLibDto> getCaseListByPage(Integer pageNum,
                                           Integer pageSize,
                                           Integer diagStatus,
                                           String[] diseaseName,
                                           String patientInfoPatientId);

    /**
     * 更新病例数据
     * @param caseDto
     * @return
     */
    CaseDto update(CaseDto caseDto);

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

    CaseDto updateNorDiag(CaseDto caseDto);

    PageBean<historyCaseListDto> getHistoryCaseListByPage(String patientId);

    List<Object[]> getNormalDiagByCaseId(String caseId);
}
