package com.itshixun.industy.fundusexamination.Service.Impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;

import com.itshixun.industy.fundusexamination.pojo.*;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseLibDto;
import com.itshixun.industy.fundusexamination.pojo.dto.historyCaseListDto;
import com.itshixun.industy.fundusexamination.repository.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.*;
import java.util.stream.Collectors;
@Transactional
@Service
public class CaseServiceImpl implements CaseService {
    @Autowired
    private OriRepository oriRepository;
    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private NormalDiagRepository normalDiagRepository;
    @Autowired
    private PatientInfoRepository patientInfoRepository;
    @Override
    public Case add(CaseDto caseDto) {
            OriginImageData origin = new OriginImageData();

            PatientInfo patient = new PatientInfo();
        // 1. 保存 OriginImageData
        if(caseDto.getOriginImageData()!=null){
            if(caseDto.getOriginImageData().getImageId()!=null){
                origin.setImageId(caseDto.getOriginImageData().getImageId());
            }
            origin.setLeftImage(caseDto.getOriginImageData().getLeftImage());
            origin.setRightImage(caseDto.getOriginImageData().getRightImage());
            oriRepository.save(origin);
        }

        //2. 保存 patientInfo
        if(caseDto.getPatientInfo()!=null){
            if(caseDto.getPatientInfo().getPatientId()!=null){
                patient = patientInfoRepository.findById(caseDto.getPatientInfo().getPatientId()).get();
            }
            patient.setName(caseDto.getPatientInfo().getName());
            patient.setGender(caseDto.getPatientInfo().getGender());
            patient.setAge(caseDto.getPatientInfo().getAge());
            patientInfoRepository.save(patient);
        }

        // 5. 保存 Case
        Case CasePojo = new Case();
        BeanUtils.copyProperties(caseDto, CasePojo);
        CasePojo.setOriginImageData(origin);

        CasePojo.setPatientInfo(patient);
        CasePojo.setIsDeleted(0);
        if(caseDto.getDiseaseName()!=null){
            CasePojo.setDiseaseName(caseDto.getDiseaseName());
        }
        return caseRepository.save(CasePojo);
    }




    @Override
    public PageBean<CaseLibDto> getCaseListByPage(
            Integer pageNum, Integer pageSize,
            Integer diagStatus, String[] diseaseName, String patientInfoPatientId
    ) {

        //如果diagStatus，diseaseType，patientInfoPatientId为-1，则设置成NULL
        if (diagStatus != null && diagStatus == -1) {
            diagStatus = null;
        }

        if ("全部".equals(diseaseName[0])) {
            diseaseName = null;
        }
        if (patientInfoPatientId.equals("")) {
            patientInfoPatientId = null;
        }
        // 1. 创建 Pageable 参数（PageRequest 是 Pageable 的子类）
        // 确保 pageNum 和 pageSize 不为 null
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("页码和每页数量不能为空");
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        //将diseaseName转换成Json字符串

        String diseaseNameJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 移除元素中的双引号（如果前端已经携带）
            if(diseaseName != null) {
                diseaseName = Arrays.stream(diseaseName)
                        .map(s -> s.replace("\"", "")) // 新增：去除每个疾病名称的双引号
                        .toArray(String[]::new);
            }
            diseaseNameJson = diseaseName != null ?
                    objectMapper.writeValueAsString(diseaseName) : null;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("疾病名称数组转换失败", e);
        }
        // 2. 调用仓库方法时传递 Pageable
        Page<Case> casePage = caseRepository.list(
                diagStatus, diseaseNameJson, patientInfoPatientId, pageable
        );
        // 3. 转换为 DTO 并封装到 PageBean
        PageBean<CaseLibDto> pageBean = convertToPageBean(casePage);
        return pageBean;
    }

    @Override
    public CaseDto update(CaseDto caseDto) {

        // 如果OriginImageData是新对象（无ID），先保存它
        OriginImageData origin = caseDto.getOriginImageData();
        if (origin != null && origin.getImageId() == null) {
            oriRepository.save(origin);
        }
        // 如果PatientInfo是新对象（无ID），先保存它
        PatientInfo patient = caseDto.getPatientInfo();
        if (patient != null && patient.getPatientId() == null) {
            patientInfoRepository.save(patient);
        }
        Case up = new Case();
        BeanUtils.copyProperties(caseDto, up);

        if(caseDto.getDiseaseName()!=null){
            up.setDiseaseName(caseDto.getDiseaseName());
        }
       caseRepository.save(up);
       BeanUtils.copyProperties(up, caseDto);
        return caseDto;
//        return null;
    }

    @Override
    public void delete(String caseId) {
        Case del = new Case();
        caseRepository.updateById(caseId);
    }

    @Override
    public boolean isPatientExist(String patientId) {
        Optional<PatientInfo> byId = patientInfoRepository.selectById(patientId);
        return byId.isPresent();

    }

    @Override
    public Case getCaseById(String caseId) {
        // 使用orElseThrow处理Optional
        Case aCase1 = caseRepository.selectById(caseId)
                .orElseThrow(() -> new RuntimeException("病例不存在 ID：" + caseId));

        return aCase1;
    }

    @Override
    public CaseDto updateNorDiag(CaseDto caseDto) {
        if(caseDto.getNormalDiag().getDocSuggestions()!=null){

            Map<String,Object> map = ThreadLocalUtil.get();
            String responsibleDoctor = (String) map.get("userName");


        }
        String caseId = caseDto.getCaseId();
        Case aCase = caseRepository.selectById(caseId)
                .orElseThrow(() -> new RuntimeException("病例不存在 ID：" + caseId));
        if(caseDto.getDiseaseName()!=null){
            aCase.setDiseaseName(caseDto.getDiseaseName());
            caseRepository.save(aCase);
        }
        if(caseDto.getNormalDiag().getDocSuggestions()!=null){

            Map<String,Object> map = ThreadLocalUtil.get();
            String responsibleDoctor = (String) map.get("userName");
            addNormalDiag(caseId, responsibleDoctor, caseDto.getNormalDiag().getDocSuggestions());
            caseDto.getNormalDiag().setDoctorName(responsibleDoctor);
        }

        // 更新diseaseName

        return caseDto;
    }

    /**
     * 根据患者id查询所有病例
     * @param patientId
     * @return
     */
    @Override
    public PageBean<historyCaseListDto> getHistoryCaseListByPage(String patientId) {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Case> casePage = caseRepository.findByPatientInfoPatientId(patientId,pageable);
        // 3. 转换为 DTO 并封装到 PageBean
        PageBean<historyCaseListDto> pageBean = convertTohisPageBean(casePage);
                return pageBean;
    }

    @Override
    public List<Object[]> getNormalDiagByCaseId(String caseId) {
        return normalDiagRepository.findNormalDiagsByCaseId(caseId);
    }

    private PageBean<CaseLibDto> convertToPageBean(Page<Case> casePage) {
        PageBean<CaseLibDto> pb = new PageBean<>();
        pb.setTotal(casePage.getTotalElements()); // 总记录数
        pb.setItems(
                casePage.getContent() // 当前页数据
                        .stream()
                        .map(this::convertToDto) // 转换为 DTO
                        .collect(Collectors.toList())
        );
        return pb;
    }

    private CaseLibDto convertToDto(Case caseEntity) {
        CaseLibDto dto = new CaseLibDto();
        BeanUtils.copyProperties(caseEntity, dto);
        //将实体类里面的diseaseNameJson转换成String[]类型
        //再存储到dto里面的diseaseName字段

        try {
            if (caseEntity.getDiseaseNameJson() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                dto.setDiseaseNameJ(objectMapper.readValue(caseEntity.getDiseaseNameJson(), String[].class));
            } else {
                dto.setDiseaseNameJ(new String[0]);  // 设置为空数组而不是 null
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("疾病名称转换失败", e);
        }


        System.out.println(dto.toString());
        return dto;
    }
    private PageBean<historyCaseListDto> convertTohisPageBean(Page<Case> casePage) {
        PageBean<historyCaseListDto> pb = new PageBean<>();
        pb.setTotal(casePage.getTotalElements()); // 总记录数
        pb.setItems(
                casePage.getContent() // 当前页数据
                        .stream()
                        .map(this::convertToHistoryDto) // 转换为 DTO
                        .collect(Collectors.toList())
        );
        return pb;
    }

    private historyCaseListDto convertToHistoryDto(Case caseEntity) {
        historyCaseListDto dto = new historyCaseListDto();
        BeanUtils.copyProperties(caseEntity, dto);
        try {
            if (caseEntity.getDiseaseNameJson() != null) {
                // 移除所有反斜杠并保留双引号
                String cleanedJson = caseEntity.getDiseaseNameJson()
                        .replaceAll("\\\\", "");  // 正则表达式匹配所有反斜杠
                dto.setDiseaseName(cleanedJson);
            } else {
                dto.setDiseaseName("[]");  // 空数组的JSON表示
            }
        } catch (Exception e) {
            throw new RuntimeException("疾病名称转换失败", e);
        }

        return dto;
    }
    public void addNormalDiag(String caseId, String doctorName, String suggestions) {
        // 1. 创建 NormalDiag 对象
        NormalDiag diag = new NormalDiag();
        diag.setDoctorName(doctorName);
        diag.setDocSuggestions(suggestions);  // 假设已正确映射医生建议字段

        // 2. 关联 Case（通过 caseId）

        Case caseEntity = caseRepository.selectById(caseId).orElseThrow(() -> new RuntimeException("Case not found"));
        diag.setCaseEntity(caseEntity);
        caseRepository.setDiagStatusById(caseId);
        // 3. 保存诊断信息
        normalDiagRepository.save(diag);
    }
}



