package com.itshixun.industy.fundusexamination.Service;

import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.ResponseData;
import org.apache.coyote.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PreImageService {
    /**
     * 保存并诊断
     * @param caseDto
     * @return
     */
    Case saveAndDiag(CaseDto caseDto);

    ResponseData sendUrltoP(String caseId,String urlLeft, String urlRight);

    Map<String, String> saveOSS(String originalFileName1, String originalFileName2,
                                InputStream inputStream1,
                                InputStream inputStream2);

    Map<String, List<MultipartFile>> pattern(MultipartFile[] files);


    PatientInfo selectPatientInfo(String patientId);
}
