package com.itshixun.industy.fundusexamination.Service;

import com.itshixun.industy.fundusexamination.pojo.dto.DiagnosedCaseDto;
import org.springframework.stereotype.Service;


public interface DiagService {
    DiagnosedCaseDto getDiag(Integer id);

    DiagnosedCaseDto addDiag(DiagnosedCaseDto diag);
}
