package com.itshixun.industy.fundusexamination.service;

import com.itshixun.industy.fundusexamination.domain.po.PageBean;
import com.itshixun.industy.fundusexamination.domain.po.PatientInfo;
import com.itshixun.industy.fundusexamination.domain.dto.PatientDTO;
import com.itshixun.industy.fundusexamination.domain.dto.PatientLibDTO;

public interface PatientService {
    PageBean<PatientLibDTO> getPatientListByPage(Integer pageNum, Integer pageSize, Integer age, String name, String patientId, Integer gender);

    PageBean<PatientLibDTO> selectPatientListByPageById(
            Integer pageNum, Integer pageSize, String target);

    PageBean<PatientLibDTO> selectPatientListByPageByName(
            Integer pageNum, Integer pageSize, String target);

    PatientInfo updatePatientInfo(PatientDTO patientDto);

    boolean delete(String patientId);

    PatientDTO add(PatientInfo patientInfo);
}
