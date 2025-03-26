package com.itshixun.industy.fundusexamination.Service.Impl;

import com.itshixun.industy.fundusexamination.Service.DiagService;
import com.itshixun.industy.fundusexamination.pojo.*;
import com.itshixun.industy.fundusexamination.pojo.dto.DiagnosedCaseDto;
import com.itshixun.industy.fundusexamination.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiagServiceImpl implements DiagService {
    @Autowired
    private OriRepository oriRepository;
    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private PatientInfoRepository patientInfoRepository;


    @Autowired
    private DiagRepository normalDiagRepository;
    @Override
    public DiagnosedCaseDto getDiag(String caseId) {

        //1. 从case中获取case，oriI，preI，patiInfo
        Case CasePojo = caseRepository.selectById(caseId).orElse(null);

        DiagnosedCaseDto diag = new DiagnosedCaseDto();
        BeanUtils.copyProperties(CasePojo, diag);
        //转换，从毫秒树到时间戳
        return diag;
    }

    @Override
    public DiagnosedCaseDto addDiag(DiagnosedCaseDto diag) {
        PatientInfo patient = new PatientInfo();

        NormalDiag nDiag = new NormalDiag();


        //2. 保存 patientInfo
        if(diag.getPatientInfo()!=null){
            patient.setName(diag.getPatientInfo().getName());
            patient.setGender(diag.getPatientInfo().getGender());
            patient.setAge(diag.getPatientInfo().getAge());
            patientInfoRepository.save(patient);
        }

        //4. 保存 normalDiag
        if(diag.getNormalDiag()!=null){
            nDiag.setDoctorName(diag.getNormalDiag().getDoctorName());
            nDiag.setDocSuggestions(diag.getNormalDiag().getDocSuggestions());
            normalDiagRepository.save(nDiag);
        }
//        caseRepository.save(diag.getCaseInfo());
        //5.保存到cases
        Case CasePojo = new Case();
        BeanUtils.copyProperties(diag, CasePojo);

//        CasePojo.setNormalDiag(nDiag);
        CasePojo.setPatientInfo(patient);

        caseRepository.save(CasePojo);
        BeanUtils.copyProperties(CasePojo, diag);
        return diag;
    }

    @Override
    public DiagnosedCaseDto updateDiag(DiagnosedCaseDto diag) {
        // 1. 初始化实体对象
        Case casePojo = new Case();

        // 2. 复制 DTO 属性到实体对象
        BeanUtils.copyProperties(diag, casePojo);

        // 3. 调用 JPA save()
        Case savedCase = caseRepository.save(casePojo);
        BeanUtils.copyProperties(savedCase, diag);
        return diag;

    }

    @Override
    public void deleteDiag(String caseId) {
        caseRepository.updateById(caseId);
    }
}
