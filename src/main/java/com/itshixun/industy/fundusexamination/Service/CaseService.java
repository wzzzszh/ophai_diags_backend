package com.itshixun.industy.fundusexamination.Service;

import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.User;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;

import java.util.List;

public interface CaseService {
    Case add(CaseDto caseDto);

    /**
     * //条件分页查询
     * @param pageNum
     * @param pageSize
     * @param diagStatus
     * @param diseaseType
     * @param patientInfoPatientId
     * @return
     */
    PageBean<CaseDto> getCaseListByPage(Integer pageNum, Integer pageSize, Integer diagStatus, Integer diseaseType, Integer patientInfoPatientId);

    /**
     * 更新病例数据
     * @param caseDto
     * @return
     */
    CaseDto update(CaseDto caseDto);

    /**
     * 逻辑删除病例数据
     * @param id
     */
    void delete(Integer id);
}
