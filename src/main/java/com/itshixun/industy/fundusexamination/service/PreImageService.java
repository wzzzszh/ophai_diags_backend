package com.itshixun.industy.fundusexamination.service;

import com.itshixun.industy.fundusexamination.domain.po.Case;
import com.itshixun.industy.fundusexamination.domain.po.PageBean;
import com.itshixun.industy.fundusexamination.domain.po.PatientInfo;
import com.itshixun.industy.fundusexamination.domain.dto.CaseDTO;
import com.itshixun.industy.fundusexamination.domain.dto.ImageDTO;
import com.itshixun.industy.fundusexamination.domain.httpEnity.ResponseData;
import jakarta.servlet.ServletOutputStream;
import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public interface PreImageService {


    /**
     * 保存并诊断
     * @param caseDto
     * @return
     */
    Case saveAndDiag(CaseDto caseDto);

    ResponseData sendUrltoP(int age,int gender,String name,String caseId,String urlLeft, String urlRight);

    Map<String, String> saveOSS(String originalFileName1, String originalFileName2,
                                InputStream inputStream1,
                                InputStream inputStream2);

    Map<String, List<MultipartFile>> pattern(MultipartFile[] files);


    PatientInfo selectPatientInfo(String patientId);

    void saveAndProcess(MultipartFile[] files) throws IOException;

    PageBean<ImageDTO> SelectImageByPage(
            Integer pageNum, Integer pageSize,
            Integer diagStatus,
            String[] diseaseName,
            Integer gender,
            Integer startAge, Integer endAge,
            LocalDateTime startDate, LocalDateTime endDate);


    void batchExportImage(Integer pageNum, Integer pageSize,
                          Integer diagStatus, String[] diseaseName,
                          Integer gender,
                          Integer startAge, Integer endAge,
                          LocalDateTime startDate, LocalDateTime endDate,
                          ZipOutputStream zipOut);

//
//    void exportData(Integer pageNum, Integer pageSize, Integer diagStatus, String[] diseaseNameArray, Integer gender, Integer startAge, Integer endAge, LocalDateTime startDate, LocalDateTime endDate, ServletOutputStream out);

    void exportDataToZip(Integer pageNum, Integer pageSize, Integer diagStatus, String[] diseaseNameArray, Integer gender, Integer startAge, Integer endAge, LocalDateTime startDate, LocalDateTime endDate, ZipOutputStream zipOut);














}
