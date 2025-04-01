package com.itshixun.industy.fundusexamination.Controller;


import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.Service.PatientService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.dto.historyCaseListDto;
import com.itshixun.industy.fundusexamination.pojo.dto.patientLibDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private CaseService caseService;
    @GetMapping("/throwList")
    public ResponseMessage<PageBean<patientLibDto>> updateCase(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) Integer gender
    ) {
            PageBean<patientLibDto> pageBean = patientService.getPatientListByPage(pageNum, pageSize, age, name, patientId, gender);
            return ResponseMessage.success(pageBean);
    }
    @GetMapping("/list")
    public ResponseMessage<PageBean<patientLibDto>> selectPatientList(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String target

    ) {
        PageBean<patientLibDto> pageBean = null;

        if(target.contains("PA_")) {
            pageBean = patientService.selectPatientListByPageById(
                    pageNum, pageSize, target);
        }
        else {
            pageBean = patientService.selectPatientListByPageByName(
                    pageNum, pageSize, target);
        }

        return ResponseMessage.success(pageBean);
    }
    @GetMapping("/batch")
    public ResponseMessage<PageBean<historyCaseListDto>> batchSelectPatientList(
            @RequestParam(required = false) String patientId){
        PageBean<historyCaseListDto> pageBean = caseService.getHistoryCaseListByPage(patientId);
        return ResponseMessage.success(pageBean);
    }
}
