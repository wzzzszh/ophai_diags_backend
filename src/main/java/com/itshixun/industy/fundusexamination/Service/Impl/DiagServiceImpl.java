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
    private PreRepository preImageRepository;
    @Autowired
    private PatientInfoRepository patientInfoRepository;
    @Autowired
    private DiseaseRateRepository diseaseRateRepository;
    @Autowired
    private AiDiagRepository aiDiagRepository;
    @Autowired
    private DiagRepository normalDiagRepository;
    @Override
    public DiagnosedCaseDto getDiag(String id) {

        //1. 从case中获取case，oriI，preI，patiInfo
        Case CasePojo = caseRepository.findById(id).orElse(null);
        DiagnosedCaseDto diag = new DiagnosedCaseDto();
        BeanUtils.copyProperties(CasePojo, diag);
        return diag;
    }

    @Override
    public DiagnosedCaseDto addDiag(DiagnosedCaseDto diag) {
        PatientInfo patient = new PatientInfo();
        AiDiag aiDiag = new AiDiag();
        NormalDiag nDiag = new NormalDiag();
        DiseaseRate rate = new DiseaseRate();
        //1. 保存 diseaseRate
        if(diag.getDiseaseRate()!=null){
            rate.setaRate(diag.getDiseaseRate().getaRate());
            rate.setdRate(diag.getDiseaseRate().getdRate());
            rate.setgRate(diag.getDiseaseRate().getgRate());
            rate.setcRate(diag.getDiseaseRate().getcRate());
            rate.sethRate(diag.getDiseaseRate().gethRate());
            rate.setOtherRate(diag.getDiseaseRate().getOtherRate());
            rate.setmRate(diag.getDiseaseRate().getmRate());
            diseaseRateRepository.save(rate);
        }
        //2. 保存 patientInfo
        if(diag.getPatientInfo()!=null){
            patient.setName(diag.getPatientInfo().getName());
            patient.setGender(diag.getPatientInfo().getGender());
            patient.setAge(diag.getPatientInfo().getAge());
            patientInfoRepository.save(patient);
        }
        //3. 保存 aiDiag
        if(diag.getAiDiag()!=null){
            aiDiag.setDiseaseType(diag.getAiDiag().getDiseaseType());
            aiDiag.setAiSuggestion1(diag.getAiDiag().getAiSuggestion1());
            aiDiag.setAiSuggestion2(diag.getAiDiag().getAiSuggestion2());
            aiDiag.setAiSuggestion3(diag.getAiDiag().getAiSuggestion3());
            aiDiag.setAiDiagnosis1(diag.getAiDiag().getAiDiagnosis1());
            aiDiag.setAiDiagnosis2(diag.getAiDiag().getAiDiagnosis2());
            aiDiag.setAiDiagnosis3(diag.getAiDiag().getAiDiagnosis3());
            aiDiagRepository.save(aiDiag);
        }
        //4. 保存 normalDiag
        if(diag.getNormalDiag()!=null){
            nDiag.setDoctorName(diag.getNormalDiag().getDoctorName());
            nDiag.setDepartment(diag.getNormalDiag().getDepartment());
            nDiag.setDocSuggestions(diag.getNormalDiag().getDocSuggestions());
            normalDiagRepository.save(nDiag);
        }
//        caseRepository.save(diag.getCaseInfo());
        //5.保存到cases
        Case CasePojo = new Case();
        BeanUtils.copyProperties(diag, CasePojo);
        CasePojo.setAiDiag(aiDiag);
        CasePojo.setNormalDiag(nDiag);
        CasePojo.setPatientInfo(patient);
        CasePojo.setDiseaseRate(rate);
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

        return diag;

    }

    @Override
    public void deleteDiag(String id) {
        caseRepository.updateById(id);
    }
}
