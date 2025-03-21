package com.itshixun.industy.fundusexamination.Service.Impl;

//import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.pojo.*;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaseServiceImpl implements CaseService {
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
    @Override
    public Case add(CaseDto caseDto) {
            OriginImageData origin = new OriginImageData();
            PreImageData pre = new PreImageData();
            DiseaseRate rate = new DiseaseRate();
            PatientInfo patient = new PatientInfo();
        // 1. 保存 OriginImageData
        if(caseDto.getOriginImageData()!=null){
            origin.setLeftImage(caseDto.getOriginImageData().getLeftImage());
            origin.setRightImage(caseDto.getOriginImageData().getRightImage());
            oriRepository.save(origin);
        }
        // 2. 保存 PreImageData
        if(caseDto.getPreImageData()!=null){
            pre.setLeftImage(caseDto.getPreImageData().getLeftImage());
            pre.setRightImage(caseDto.getPreImageData().getRightImage());
            preImageRepository.save(pre);
        }
        //3. 保存 diseaseRate
        if(caseDto.getDiseaseRate()!=null){
            rate.setaRate(caseDto.getDiseaseRate().getaRate());
            rate.setdRate(caseDto.getDiseaseRate().getaRate());
            rate.setgRate(caseDto.getDiseaseRate().getaRate());
            rate.setcRate(caseDto.getDiseaseRate().getaRate());
            rate.sethRate(caseDto.getDiseaseRate().getaRate());
            rate.setOtherRate(caseDto.getDiseaseRate().getaRate());
            rate.setmRate(caseDto.getDiseaseRate().getaRate());
            diseaseRateRepository.save(rate);
        }
        //4. 保存 patientInfo
        if(caseDto.getPatientInfo()!=null){
            patient.setName(caseDto.getPatientInfo().getName());
            patient.setGender(caseDto.getPatientInfo().getGender());
            patient.setAge(caseDto.getPatientInfo().getAge());
            patientInfoRepository.save(patient);
        }
        // 5. 保存 Case
        Case CasePojo = new Case();
        BeanUtils.copyProperties(caseDto, CasePojo);
        CasePojo.setOriginImageData(origin);
        CasePojo.setPreImageData(pre);
        CasePojo.setPatientInfo(patient);
        CasePojo.setDiseaseRate(rate);
        CasePojo.setIsDeleted(0);
        return caseRepository.save(CasePojo);
    }




    @Override
    public PageBean<CaseDto> getCaseListByPage(
            Integer pageNum, Integer pageSize,
            Integer diagStatus, Integer diseaseType, String patientInfoPatientId
    ) {
        // 1. 创建 Pageable 参数（PageRequest 是 Pageable 的子类）
        // 确保 pageNum 和 pageSize 不为 null
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("页码和每页数量不能为空");
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        System.out.println(pageable);

        // 2. 调用仓库方法时传递 Pageable
        Page<Case> casePage = caseRepository.list(
                diagStatus, diseaseType, patientInfoPatientId, pageable
        );
        System.out.println(casePage);

        //3.仓库方法是成功的
        System.out.println("Total: " + casePage.getTotalElements());
        System.out.println("Content: " + casePage.getContent());
        // 4. 转换为 DTO 并封装到 PageBean
        PageBean<CaseDto> pageBean = convertToPageBean(casePage);
        return pageBean;
    }

    @Override
    public CaseDto update(CaseDto caseDto) {

        // 如果OriginImageData是新对象（无ID），先保存它
        OriginImageData origin = caseDto.getOriginImageData();
        if (origin != null && origin.getImageId() == null) {
            oriRepository.save(origin);
        }
        // 如果PreImageData是新对象（无ID），先保存它
        PreImageData pre = caseDto.getPreImageData();
        if (pre != null && pre.getImageId() == null) {
            preImageRepository.save(pre);
        }
        // 如果PatientInfo是新对象（无ID），先保存它
        PatientInfo patient = caseDto.getPatientInfo();
        if (patient != null && patient.getPatientId() == null) {
            patientInfoRepository.save(patient);
        }
        Case up = new Case();
        BeanUtils.copyProperties(caseDto, up);
        System.out.println(up.toString());
        caseRepository.save(up);
        BeanUtils.copyProperties(up, caseDto);
        return caseDto;
    }

    @Override
    public void delete(String caseId) {
        Case del = new Case();
        caseRepository.updateById(caseId);
    }

    @Override
    public boolean isPatientExist(String patientId) {
        Optional<PatientInfo> byId = patientInfoRepository.findById(patientId);
        return byId.isPresent();

    }

    @Override
    public Case getCaseById(String caseId) {
        // 使用orElseThrow处理Optional
        Case aCase1 = caseRepository.selectById(caseId)
                .orElseThrow(() -> new RuntimeException("病例不存在 ID：" + caseId));
        System.out.println("查询的case实体在这里" + aCase1.toString());
        return aCase1;
    }

    private PageBean<CaseDto> convertToPageBean(Page<Case> casePage) {
        PageBean<CaseDto> pb = new PageBean<>();
        pb.setTotal(casePage.getTotalElements()); // 总记录数
        pb.setItems(
                casePage.getContent() // 当前页数据
                        .stream()
                        .map(this::convertToDto) // 转换为 DTO
                        .collect(Collectors.toList())
        );
        return pb;
    }

    private CaseDto convertToDto(Case caseEntity) {
        CaseDto dto = new CaseDto();
        BeanUtils.copyProperties(caseEntity, dto);
        return dto;
    }
}



