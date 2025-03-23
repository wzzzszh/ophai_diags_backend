package com.itshixun.industy.fundusexamination.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
//import com.itshixun.industy.fundusexamination.pojo.dto.CaseLibDto;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseLibDto;
import com.itshixun.industy.fundusexamination.pojo.dto.JcaseDto;
import com.itshixun.industy.fundusexamination.pojo.dto.historyCaseListDto;
import org.hibernate.query.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/case")
public class CaseContoller {
    @Autowired
    private CaseService caseService;
    //分页查询病例列表
    @GetMapping("/list")
    public ResponseMessage <PageBean<CaseLibDto>> getCaseListByPage(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer diagStatus,
            @RequestParam(required = false) Integer diseaseType,
            @RequestParam(required = false) String patientInfoPatientId
    ) {
        PageBean<CaseLibDto> pb = caseService.getCaseListByPage(pageNum,pageSize,diagStatus,diseaseType,patientInfoPatientId);
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
        Case casePojo = caseService.getCaseById(caseId);
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
            //放置json和历史病例
            jcaseDto.setAiCaseInfoJson(jsonNode);
            jcaseDto.setHistoryCaseListDto(pb);
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
