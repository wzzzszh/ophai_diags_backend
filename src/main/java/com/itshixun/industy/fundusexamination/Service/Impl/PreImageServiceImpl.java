package com.itshixun.industy.fundusexamination.Service.Impl;

import com.itshixun.industy.fundusexamination.Service.PreImageService;
import com.itshixun.industy.fundusexamination.Utils.AliOssUtil;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.ResponseData;
import com.itshixun.industy.fundusexamination.repository.CaseRepository;
import com.itshixun.industy.fundusexamination.repository.PreImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.*;

@Service
public class PreImageServiceImpl implements PreImageService {
    @Autowired
    private PreImageRepository preImageRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public Case saveAndDiag(CaseDto caseDto) {
        return null;
    }

    @Override
    public ResponseData sendUrltoP(String caseId, String urlLeft, String urlRight) {
        String apiUrl = "http://algorithm-service/api/process";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //赋值
        String leftKey = caseId + "_Left";
        String rightKey = caseId + "_Right";
        //
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(leftKey, urlLeft);
        requestBody.put(rightKey, urlRight);
        //发送请求体
        ResponseEntity<String> response = restTemplate.postForEntity(
                apiUrl,
                requestBody,
                String.class
        );
        String r = (String) response.getBody();
        return new ResponseData(r,200);
    }

    @Override
    public Map<String, String> saveOSS(String originalFileName1, String originalFileName2,InputStream fileLeft,
                                       InputStream fileRight) {
        if (originalFileName1 == null ||
                originalFileName1.isEmpty()||
                originalFileName2 == null ||
                originalFileName2.isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }
        //保证文件名唯一
        String filename1 = UUID.randomUUID().toString() + originalFileName1.substring(originalFileName1.lastIndexOf("."));
        String filename2 = UUID.randomUUID().toString() + originalFileName2.substring(originalFileName2.lastIndexOf("."));
        //保存图片信息到OSS
        String urlLeft = AliOssUtil.uploadFile(filename1, fileLeft);
        String urlRight = AliOssUtil.uploadFile(filename2, fileRight);
        //保存图片信息到url
        Map<String, String> url = new HashMap<>();
        url.put("urlLeft",urlLeft);
        url.put("urlRight",urlRight);
        return url;
    }
}
