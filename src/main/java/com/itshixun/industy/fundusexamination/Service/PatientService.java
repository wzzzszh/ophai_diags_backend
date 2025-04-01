package com.itshixun.industy.fundusexamination.Service;

import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.dto.patientLibDto;

public interface PatientService {
    PageBean<patientLibDto> getPatientListByPage(Integer pageNum, Integer pageSize, Integer age, String name, String patientId, Integer gender);

    PageBean<patientLibDto> selectPatientListByPageById(Integer pageNum, Integer pageSize, String target);

    PageBean<patientLibDto> selectPatientListByPageByName(Integer pageNum, Integer pageSize, String target);
}
