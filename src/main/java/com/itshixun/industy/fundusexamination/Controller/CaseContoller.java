package com.itshixun.industy.fundusexamination.Controller;

import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
//import com.itshixun.industy.fundusexamination.pojo.dto.CaseLibDto;
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
    public ResponseMessage <PageBean<CaseDto>> getCaseListByPage(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer diagStatus,
            @RequestParam(required = false) Integer diseaseType,
            @RequestParam(required = false) String patientInfoPatientId
    ) {
        PageBean<CaseDto> pb = caseService.getCaseListByPage(pageNum,pageSize,diagStatus,diseaseType,patientInfoPatientId);
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
    public ResponseMessage<CaseDto> getCaseById(String caseId) {
        Case casePojo = caseService.getCaseById(caseId);
        if (casePojo == null) {
            return ResponseMessage.allError(416,"病例不存在");
        }
        CaseDto caseDto = new CaseDto();
        BeanUtils.copyProperties(casePojo,caseDto);
        return ResponseMessage.success(caseDto);
    }

}
