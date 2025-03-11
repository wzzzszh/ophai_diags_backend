package com.itshixun.industy.fundusexamination.Controller;

import com.itshixun.industy.fundusexamination.Service.DiagService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
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

    // 新增综合诊断信息
    @PostMapping
    public ResponseMessage<DiagnosedCaseDto> addDiag(@RequestBody DiagnosedCaseDto diag) {
        diagService.addDiag(diag);
        return ResponseMessage.success(diag);
    }
    // 查询综合诊断信息
    @GetMapping
    public ResponseMessage<DiagnosedCaseDto> getDiag(@PathVariable Integer id) {
        DiagnosedCaseDto diag = diagService.getDiag(id);
        return ResponseMessage.success(diag);
    }
    // 查询综合诊断信息


}
