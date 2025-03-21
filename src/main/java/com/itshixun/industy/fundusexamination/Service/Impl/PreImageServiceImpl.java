package com.itshixun.industy.fundusexamination.Service.Impl;

import com.itshixun.industy.fundusexamination.Service.PreImageService;
import com.itshixun.industy.fundusexamination.Utils.AliOssUtil;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.DrugRecommendation;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.ResponseData;
import com.itshixun.industy.fundusexamination.repository.CaseRepository;
import com.itshixun.industy.fundusexamination.repository.PreImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        //
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("urlLeft", urlLeft);
        requestBody.put("urlRight", urlRight);
        //发送请求体
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                apiUrl,
//                requestBody,
//                String.class
//        );
//
        //获取返回结果
//        String r = response.getBody();
        // 硬编码模拟响应
        String mockResponse = "{"
                + "\"success\": true, "
                + "\"message\": {"
                + "\"predictions\": {\"D\": 0.85},"
                + "\"left_heatmap_url\": \"/mock/left.jpg\","
                + "\"right_heatmap_url\": \"/mock/right.jpg\","
                + "\"suggestions\": [\"模拟建议1\", \"模拟建议2\"],"
                + "\"drags\": [{\"function\":\"模拟功能\",\"drag\":[\"d模拟\"]}]"
                + "}}";
        //将所有属性保存到case里面
        return new ResponseData(mockResponse,true);
    }

    /**已经弃用
     * @status 弃用
     * @param originalFileName1
     * @param originalFileName2
     * @param fileLeft
     * @param fileRight
     * @return
     */
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
    @Override
    public Map<String, List<MultipartFile>> pattern(MultipartFile[] files) {
        Map<String, List<MultipartFile>> fileGroups = new HashMap<>();
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_]+)_(left|right)\\.\\w+$");

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            Matcher matcher = pattern.matcher(fileName);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid filename format: " + fileName);
            }

            String patientId = matcher.group(1);
            String type = matcher.group(2);
            fileGroups.computeIfAbsent(patientId, k -> new ArrayList<>()).add(file);
        }

        return fileGroups;
    }
}
