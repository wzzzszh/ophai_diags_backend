package com.itshixun.industy.fundusexamination.controller;

import com.itshixun.industy.fundusexamination.annotation.UserPermission;
import com.itshixun.industy.fundusexamination.service.CaseService;
import com.itshixun.industy.fundusexamination.utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.exception.GlobalExceptionHanderAdvice;
import com.itshixun.industy.fundusexamination.domain.po.Case;
import com.itshixun.industy.fundusexamination.domain.enums.UserPermissionEnum;
import com.itshixun.industy.fundusexamination.domain.po.NormalDiag;
import com.itshixun.industy.fundusexamination.domain.po.PageBean;
import com.itshixun.industy.fundusexamination.domain.dto.*;
import com.itshixun.industy.fundusexamination.repository.NormalDiagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/case")
public class CaseContoller {
    @Autowired
    private CaseService caseService;
    @Autowired
    private NormalDiagRepository normalDiagRepository;

    /**
     * 分页查询病例列表
     * @param pageNum
     * @param pageSize
     * @param diagStatus
     * @param diseaseName
     * @param patientInfoPatientId
     * @return
     */
    @UserPermission({UserPermissionEnum.ADMIN,UserPermissionEnum.DOCTOR})
    @GetMapping("/list")
    public ResponseMessage <PageBean<CaseLibDTO>> getCaseListByPage(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer diagStatus,
            @RequestParam(required = false) String diseaseName,
            @RequestParam(required = false) String patientInfoPatientId
    ) {

        String[] diseaseNameArray = diseaseName.split(",");
        PageBean<CaseLibDTO> pb = caseService.getCaseListByPage(pageNum,pageSize,diagStatus,diseaseNameArray,patientInfoPatientId);
        if(pb.getTotal()==0){
            return ResponseMessage.allError(409,"没有查询到病例");
        }
        return ResponseMessage.success(pb);
    }

    /**
     *  添加病例数据
     * @param caseDto 病例数据
     * @return
     */
//    @UserPermission(UserPermissionEnum.PATIENT)
    @PostMapping
    public ResponseMessage<CaseDTO> addCase(@Validated @RequestBody CaseDTO caseDto) {
        Case CaseNew = caseService.add(caseDto);
        BeanUtils.copyProperties(CaseNew, caseDto);
        return ResponseMessage.success(caseDto);
    }
    //修改病例
    //废弃
    @PutMapping("/update")
    public ResponseMessage<CaseDTO> updateCase(@Validated @RequestBody CaseDTO caseDto) {
        CaseDTO CaseNew;
        if(caseDto.getNormalDiag().getDocSuggestions()!=null){
            NormalDiag normalDiag = new NormalDiag();
            Map<String,Object> map = ThreadLocalUtil.get();
            String responsibleDoctor = (String) map.get("userName");
            Logger log = LoggerFactory.getLogger(GlobalExceptionHanderAdvice.class);
            log.error("Responsible Doctor: {}", responsibleDoctor); // 打印到控制台

            normalDiag.setDoctorName(responsibleDoctor);
            normalDiag.setDocSuggestions(caseDto.getNormalDiag().getDocSuggestions());
            normalDiag.setDoctorName(caseDto.getNormalDiag().getDoctorName());
            Case c = new Case();
            c.setCaseId(caseDto.getCaseId());
            normalDiag.setCaseEntity(c);
            normalDiagRepository.save(normalDiag);
        }
        CaseNew = caseService.update(caseDto);
        return ResponseMessage.success(CaseNew);
    }

    /**
     *  删除病例数据
     * @param caseId
     * @return
     */
    @UserPermission(UserPermissionEnum.DOCTOR)
    @PutMapping("/delete/{caseId}")
    public ResponseMessage<CaseDTO> deleteCase(@PathVariable String caseId) {
        caseService.delete(caseId);
        return ResponseMessage.success("删除病例成功");
    }

    /**
     * 查询单个病例数据
     * @param caseId
     * @return
     */
    @UserPermission({UserPermissionEnum.DOCTOR})
    @GetMapping("/simple/{caseId}")
    public ResponseMessage<JCaseDTO> getCaseById(@PathVariable String caseId) {
        JCaseDTO jcaseDto = caseService.getRealCaseById(caseId);
        return ResponseMessage.success(jcaseDto);
    }

    /**
     * 更新病例的医嘱信息
     * @param caseDto
     * @return
     */
    @UserPermission(UserPermissionEnum.DOCTOR)
    @PostMapping("/update")
    public ResponseMessage<String> updateNorCase(@RequestBody CaseUpdateDTO caseDto) {
        CaseUpdateDTO CaseNew;
        CaseNew = caseService.updateNorDiag(caseDto.getCaseId(),caseDto);
        if(CaseNew.getDiagStatus()==2) {
            return ResponseMessage.success("修改医嘱成功");
        }
        else
        {
            return ResponseMessage.allError(455,"修改医嘱失败");
        }
    }
    //查询单个患者id历史病例(测试使用)
    @GetMapping("/simpleHis/{patientId}")
    public ResponseMessage<PageBean<HistoryCaseListDTO>> getCaseByPatientId(@PathVariable String patientId) {
        PageBean<HistoryCaseListDTO> pb = caseService.getHistoryCaseListByPage(patientId);
        return ResponseMessage.success(pb);
    }
}
