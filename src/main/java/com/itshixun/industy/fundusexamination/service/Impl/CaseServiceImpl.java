package com.itshixun.industy.fundusexamination.service.Impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itshixun.industy.fundusexamination.annotation.AddCache;
import com.itshixun.industy.fundusexamination.annotation.DelCache;
import com.itshixun.industy.fundusexamination.domain.po.*;
import com.itshixun.industy.fundusexamination.service.CaseService;
import com.itshixun.industy.fundusexamination.utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.domain.dto.*;
import com.itshixun.industy.fundusexamination.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private MarkRepository markRepository;
    @Override
    public Case add(CaseDTO caseDto) {
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
    public PageBean<CaseLibDTO> getCaseListByPage(
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
        PageBean<CaseLibDTO> pageBean = convertToPageBean(casePage);
        return pageBean;
    }

    @Override
    public CaseDTO update(CaseDTO caseDto) {

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
    @DelCache(prefix = "case")
    @Transactional(rollbackOn = Exception.class)
    @Override
    public void delete(String caseId) {
        //1.删除case记录
        caseRepository.updateById(caseId);
        //2.删除mask标注记录
        markRepository.deleteById(caseId);
        //3.删除normalDiag记录
        normalDiagRepository.deleteById(caseId);
        //4.删除originImageData记录
        oriRepository.deleteById(caseId);

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
                .orElseThrow(() -> new BusinessException(421, caseId + " 该病例不存在"));

        return aCase1;
    }
    @DelCache(prefix = "case")
    @Override
    @Transactional
    public CaseUpdateDTO updateNorDiag(String caseId,CaseUpdateDTO caseDto) {
        //0.提取属性
        List<Mark> marks = caseDto.getMarks();
        String docSuggestions = caseDto.getNormalDiag().getDocSuggestions();
        //1.查询caseId是否存在
        Case aCase = caseRepository.selectById(caseId)
                .orElseThrow(() -> new RuntimeException("病例不存在 ID：" + caseId));
        //2.查询caseId是否已经诊断过,赋值
        if(caseDto.getDiseaseName()!=null){
            aCase.setDiseaseName(caseDto.getDiseaseName());
            caseRepository.save(aCase);
        }
        if(caseDto.getNormalDiag().getDocSuggestions()!=null){
            Map<String,Object> map = ThreadLocalUtil.get();
            String responsibleDoctor = (String) map.get("userName");
            //放置医嘱以及状态转换
            addNormalDiag(caseId, responsibleDoctor, docSuggestions,marks);
            caseDto.getNormalDiag().setDoctorName(responsibleDoctor);
            caseDto.setDiagStatus(2);
        }

        // 更新diseaseName

        return caseDto;
    }

    /**
     * 根据患者id查询所有病例
     * @param patientId
     * @return
     */
    @AddCache(prefix = "case:patient",expire = 60)
    @Override
    public PageBean<HistoryCaseListDTO> getHistoryCaseListByPage(String patientId) {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Case> casePage = caseRepository.findByPatientInfoPatientId(patientId,pageable);
        // 3. 转换为 DTO 并封装到 PageBean
        PageBean<HistoryCaseListDTO> pageBean = convertTohisPageBean(casePage);
//        System.out.println(pageBean.toString());
                return pageBean;
    }

    @Override
    public List<Object[]> getNormalDiagByCaseId(String caseId) {
        return normalDiagRepository.findNormalDiagsByCaseId(caseId);
    }

    @Override
    public List<Mark> getMarksByCaseId(String caseId) {
        return markRepository.findAllByCaseEntity_caseId(caseId);
    }

    /**
     * 根据caseId查询病例详情
     * @param caseId
     * @return
     */
    @AddCache(prefix = "case")
    @Override
    public JCaseDTO getRealCaseById(String caseId) {
        Case casePojo = getCaseById(caseId);
        List<Object[]> normalDiagList = getNormalDiagByCaseId(caseId);
        List<NormalDiagDTO> normalDiagObjList = new ArrayList<>();
        for (Object[] objArray : normalDiagList) {
            NormalDiagDTO normalDiag = new NormalDiagDTO();
            // 假设Object[]数组中的元素顺序与NormalDiag属性顺序对应
            normalDiag.setCreateDate((LocalDateTime) objArray[0]);
            normalDiag.setNDiagId((String) objArray[1]);
            normalDiag.setDocSuggestions((String) objArray[2]);
            normalDiag.setDoctorName((String) objArray[3]);
            normalDiag.setUpdateDate((LocalDateTime) objArray[4]);
            // 其他属性赋值...
            normalDiagObjList.add(normalDiag);
        }

        String[] diseaseName = casePojo.getDiseaseName();
        String patientId = casePojo.getPatientInfo().getPatientId();
        PageBean<HistoryCaseListDTO> pb = getHistoryCaseListByPage(patientId);
        // 过滤当前病例ID
        List<HistoryCaseListDTO> filteredList = pb.getItems().stream()
                .filter(dto -> !dto.getCaseId().equals(casePojo.getCaseId()))
                .collect(Collectors.toList());

        // 创建新的分页对象
        PageBean<HistoryCaseListDTO> filteredPb = new PageBean<>();
        filteredPb.setTotal(pb.getTotal() - 1); // 总数减1
        filteredPb.setItems(filteredList);

        if (casePojo == null) {
            throw new BusinessException(416,"病例不存在");
        }
        String jsonNodeStr = casePojo.getAiCaseInfo();
        ObjectMapper objectMapper = new ObjectMapper();
        JCaseDTO jcaseDto = new JCaseDTO();
        List<Mark> marks = getMarksByCaseId(caseId);
        // 将marks中的所有CaseEntity设置为null
        for (Mark mark : marks) {
            mark.setCaseEntity(null);
        }
        //往dto里面放数据
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonNodeStr);
            BeanUtils.copyProperties(casePojo,jcaseDto);
            //放置json和历史病例、疾病名
            jcaseDto.setDiseaseName(diseaseName);
            jcaseDto.setAiCaseInfoJson(jsonNode);
            jcaseDto.setHistoryCaseListDto(filteredPb);
            jcaseDto.setDoctorDiags(normalDiagObjList);
            jcaseDto.setMarks(marks);
            // 现在你可以使用 jsonNode 对象进行后续操作
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(420,"json解析失败");
        }
        return jcaseDto;
    }

    private PageBean<CaseLibDTO> convertToPageBean(Page<Case> casePage) {
        PageBean<CaseLibDTO> pb = new PageBean<>();
        pb.setTotal(casePage.getTotalElements()); // 总记录数
        pb.setItems(
                casePage.getContent() // 当前页数据
                        .stream()
                        .map(this::convertToDto) // 转换为 DTO
                        .collect(Collectors.toList())
        );
        return pb;
    }

    private CaseLibDTO convertToDto(Case caseEntity) {
        CaseLibDTO dto = new CaseLibDTO();
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

        return dto;
    }
    private PageBean<HistoryCaseListDTO> convertTohisPageBean(Page<Case> casePage) {
        PageBean<HistoryCaseListDTO> pb = new PageBean<>();
        pb.setTotal(casePage.getTotalElements()); // 总记录数
        pb.setItems(
                casePage.getContent() // 当前页数据
                        .stream()
                        .map(this::convertToHistoryDto) // 转换为 DTO
                        .collect(Collectors.toList())
        );
        return pb;
    }

    private HistoryCaseListDTO convertToHistoryDto(Case caseEntity) {
        HistoryCaseListDTO dto = new HistoryCaseListDTO();
        BeanUtils.copyProperties(caseEntity, dto);
        try {
            if (caseEntity.getDiseaseNameJson() != null) {
                ObjectMapper mapper = new ObjectMapper();
                String[] diseaseArray = mapper.readValue(caseEntity.getDiseaseNameJson(), String[].class);

                // 处理 ["null"] 的特殊情况

                if(diseaseArray == null || (diseaseArray.length == 1 && "null".equals(diseaseArray[0]))){
                    dto.setDiseaseName(new String[0]);  // 设置为空数组
                    System.out.println("空数组"+diseaseArray);
                } else {
                    dto.setDiseaseName(diseaseArray);
                    System.out.println("非空"+diseaseArray);// 直接赋值数组
                }
            } else {
                dto.setDiseaseName(new String[0]);  // 空数组
            }
        } catch (Exception e) {
            throw new RuntimeException("疾病名称转换失败", e);
        }

        return dto;
    }
    @Transactional
    public void addNormalDiag(String caseId, String doctorName, String suggestions,List<Mark> marks) {
        // 1. 创建 NormalDiag 对象

        NormalDiag diag = new NormalDiag();
        if(doctorName != null){
            diag.setDoctorName(doctorName);
        }
        if(suggestions != null){
            diag.setDocSuggestions(suggestions);  // 假设已正确映射医生建议字段
        }

        // 2. 关联 Case（通过 caseId）
        Case caseEntity = caseRepository.selectById(caseId).orElseThrow(() -> new BusinessException(416,"病例不存在"));
        diag.setCaseEntity(caseEntity);
        caseRepository.setDiagStatusById(caseId);
        // 3.放置Mask标注
        if(marks != null){
            for (Mark mark : marks) {
                mark.setCaseEntity(caseEntity);
                markRepository.save(mark);
            }
        }
        // 4. 保存该病例的诊断信息
        if(diag != null){
            normalDiagRepository.save(diag);
        }

    }
}



