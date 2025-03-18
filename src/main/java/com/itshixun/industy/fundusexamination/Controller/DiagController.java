package com.itshixun.industy.fundusexamination.Controller;

import com.itshixun.industy.fundusexamination.Service.DiagService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.dto.DiagnosedCaseDto;
import jdk.jshell.Diag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diag")
public class DiagController {
    @Autowired
    private DiagService diagService;

    // 新增综合诊断信息(已经弃用)
    @PostMapping
    public ResponseMessage<DiagnosedCaseDto> addDiag(@RequestBody DiagnosedCaseDto diag) {
        diagService.addDiag(diag);
        return ResponseMessage.success(diag);
    }
    // 查询综合诊断信息
    @GetMapping("/{caseId}")
    public ResponseMessage<DiagnosedCaseDto> getDiag(@PathVariable String caseId) {
        DiagnosedCaseDto diag = diagService.getDiag(caseId);
        return ResponseMessage.success(diag);
    }
    // 更新综合诊断信息
    @PutMapping
    public ResponseMessage<DiagnosedCaseDto> updateDiag(@RequestBody DiagnosedCaseDto diag) {
        diagService.updateDiag(diag);
        return ResponseMessage.success(diag);
    }
    // 删除综合诊断信息
    @DeleteMapping("/{id}")
    public ResponseMessage<Boolean> deleteDiag(@PathVariable String id) {
        diagService.deleteDiag(id);
        return ResponseMessage.success(true);
    }
}
