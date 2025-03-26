package com.itshixun.industy.fundusexamination.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.NormalDiag;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.dto.*;
//import com.itshixun.industy.fundusexamination.pojo.dto.CaseLibDto;
import org.hibernate.query.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/case")
public class CaseContoller {
    @Autowired
    private CaseService caseService;
    //分页查询病例列表
    @GetMapping("/list")
    @CrossOrigin
    public ResponseMessage <PageBean<CaseLibDto>> getCaseListByPage(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer diagStatus,
            @RequestParam(required = false) String[] diseaseName,
            @RequestParam(required = false) String patientInfoPatientId
    ) {
        PageBean<CaseLibDto> pb = caseService.getCaseListByPage(pageNum,pageSize,diagStatus,diseaseName,patientInfoPatientId);
        return ResponseMessage.success(pb);
    }
    //添加病例
    @PostMapping
    public ResponseMessage<CaseDto> addCase(@Validated @RequestBody CaseDto caseDto) {
        Case CaseNew;
        CaseNew = caseService.add(caseDto);
        BeanUtils.copyProperties(CaseNew, caseDto);
        return ResponseMessage.success(caseDto);
    }
    //修改病例
    @PutMapping("/update")
    public ResponseMessage<CaseDto> updateCase(@Validated @RequestBody CaseDto caseDto) {
        CaseDto CaseNew;
        CaseNew = caseService.update(caseDto);
        return ResponseMessage.success(CaseNew);
    }
    //删除病例
    @PutMapping("/delete/{caseId}")
    public ResponseMessage<CaseDto> deleteCase(@PathVariable String caseId) {
//        Case CaseNew;
//        CaseNew = caseService.delete(caseDto);
        caseService.delete(caseId);
        return ResponseMessage.success("删除病例成功");
    }
    //查询单个病例
    @GetMapping("/simple/{caseId}")
    public ResponseMessage<JcaseDto> getCaseById(@PathVariable String caseId) {
        //先查询病例库,并且得到jsonDName,获得历史医嘱列表
        Case casePojo = caseService.getCaseById(caseId);
        List<Object[]> normalDiagList = caseService.getNormalDiagByCaseId(caseId);
        List<NormalDiagDto> normalDiagObjList = new ArrayList<>();
        for (Object[] objArray : normalDiagList) {
            NormalDiagDto normalDiag = new NormalDiagDto();
            // 假设Object[]数组中的元素顺序与NormalDiag属性顺序对应
            // 这里需要根据实际情况调整索引和赋值逻辑
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
        PageBean<historyCaseListDto> pb = caseService.getHistoryCaseListByPage(patientId);

        if (casePojo == null) {
            return ResponseMessage.allError(416,"病例不存在");
        }
        String jsonNodeStr = casePojo.getAiCaseInfo();
        ObjectMapper objectMapper = new ObjectMapper();
        JcaseDto jcaseDto = new JcaseDto();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonNodeStr);
            BeanUtils.copyProperties(casePojo,jcaseDto);
            //放置json和历史病例、疾病名
            jcaseDto.setDiseaseName(diseaseName);
            jcaseDto.setAiCaseInfoJson(jsonNode);
            jcaseDto.setHistoryCaseListDto(pb);
            jcaseDto.setDoctorDiags(normalDiagObjList);
            // 现在你可以使用 jsonNode 对象进行后续操作
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jcaseDto);
        return ResponseMessage.success(jcaseDto);
    }
    //更新病例的医嘱信息
    @PostMapping("/update")
    public ResponseMessage<String> updateNorCase(@RequestBody CaseDto caseDto) {
        CaseDto CaseNew;
        CaseNew = caseService.updateNorDiag(caseDto);
        return ResponseMessage.success("修改医嘱成功");
    }
    //查询单个患者id历史病例(测试使用)
    @GetMapping("/simpleHis/{patientId}")
    public ResponseMessage<PageBean<historyCaseListDto>> getCaseByPatientId(@PathVariable String patientId) {
        PageBean<historyCaseListDto> pb = caseService.getHistoryCaseListByPage(patientId);
        return ResponseMessage.success(pb);
    }
}
