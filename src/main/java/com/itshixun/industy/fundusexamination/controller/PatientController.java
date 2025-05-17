package com.itshixun.industy.fundusexamination.controller;


import com.itshixun.industy.fundusexamination.annotation.UserPermission;
import com.itshixun.industy.fundusexamination.domain.dto.HistoryCaseListDTO;
import com.itshixun.industy.fundusexamination.domain.dto.PatientDTO;
import com.itshixun.industy.fundusexamination.domain.dto.PatientLibDTO;
import com.itshixun.industy.fundusexamination.domain.enums.UserPermissionEnum;
import com.itshixun.industy.fundusexamination.domain.po.PageBean;
import com.itshixun.industy.fundusexamination.domain.po.PatientInfo;
import com.itshixun.industy.fundusexamination.service.CaseService;
import com.itshixun.industy.fundusexamination.service.PatientService;
import com.itshixun.industy.fundusexamination.utils.ResponseMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private CaseService caseService;
    @GetMapping("/throwList")
    public ResponseMessage<PageBean<PatientLibDTO>> updateCase(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) Integer gender
    ) {
            PageBean<PatientLibDTO> pageBean = patientService.getPatientListByPage(pageNum, pageSize, age, name, patientId, gender);
            return ResponseMessage.success(pageBean);
    }
    @GetMapping("/list")
    public ResponseMessage<PageBean<PatientLibDTO>> selectPatientList(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String target

    ) {
        PageBean<PatientLibDTO> pageBean = null;

        if(target.contains("PA_")) {
            pageBean = patientService.selectPatientListByPageById(
                    pageNum, pageSize, target);
        }
        else {
            pageBean = patientService.selectPatientListByPageByName(
                    pageNum, pageSize, target);
        }
        if(pageBean.getTotal()==0) {
            return ResponseMessage.allError(408,"没有查询到患者");
        }
        return ResponseMessage.success(pageBean);
    }

    /**
     * 根据patientId查询历史病例库列表
     * @param patientId
     * @return
     */
    @GetMapping("/batch")
    public ResponseMessage<PageBean<HistoryCaseListDTO>> batchSelectPatientList(
             String patientId){
        PageBean<HistoryCaseListDTO> pageBean = caseService.getHistoryCaseListByPage(patientId);
        if(pageBean.getTotal()==0) {
            return ResponseMessage.allError(408,"没有查询到病例");
        }
        return ResponseMessage.success(pageBean);
    }
    /**
     * 修改患者信息
     * @param patientDto
     * @return
     */
    @UserPermission(UserPermissionEnum.ADMIN)
    @PostMapping("/update")
    public ResponseMessage<PatientDTO> updatePatient(@RequestBody PatientDTO patientDto) {

        PatientInfo patientInfo = patientService.updatePatientInfo(patientDto);
        patientDto.setCreateDate(patientInfo.getCreateDate());
        return ResponseMessage.success(patientDto);
    }
    /**
     * 删除患者信息
     * @param patientId
     * @return
     */
    @UserPermission(UserPermissionEnum.ADMIN)
    @GetMapping("delete")
    public ResponseMessage<PatientDTO> deletePatient(@RequestParam String patientId) {
        if(patientService.delete(patientId)){
            return ResponseMessage.success(patientId);
        }
        return ResponseMessage.allError(521,patientId);
    }
    /**
     * 添加患者信息
     * @param patientDto
     * @return
     */
    @UserPermission(UserPermissionEnum.ADMIN)
    @PostMapping("/add")
    public ResponseMessage<PatientDTO> addPatient(@RequestBody PatientDTO patientDto) {
        PatientInfo patientInfo = new PatientInfo();
        BeanUtils.copyProperties(patientDto, patientInfo);
        patientInfo.setGender(patientDto.getGender());
        patientInfo.setAge(patientDto.getAge());
        patientInfo.setName(patientDto.getName());
        patientDto = patientService.add(patientInfo);
        return ResponseMessage.success(patientDto);
    }

}
