package com.itshixun.industy.fundusexamination.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itshixun.industy.fundusexamination.Service.PreImageService;
import com.itshixun.industy.fundusexamination.Utils.AliOssUtil;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.DrugRecommendation;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.ResponseData;
import com.itshixun.industy.fundusexamination.repository.CaseRepository;
import com.itshixun.industy.fundusexamination.repository.PatientInfoRepository;
import com.itshixun.industy.fundusexamination.repository.PreImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
    private PatientInfoRepository patientInfoRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public Case saveAndDiag(CaseDto caseDto) {
        return null;
    }

    @Override
    public ResponseData sendUrltoP(int age ,int gender ,String name, String caseId, String urlLeft, String urlRight) {
        String apiUrl =
                "https://1a2f-222-195-190-92.ngrok-free.app/api/process-images/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("ngrok-skip-browser-warning", "true"); // 绕过ngrok警告
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        //
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("age", age);  // 直接使用int类型
        requestBody.put("gender", gender);  // 直接使用int类型
        requestBody.put("left_url", urlLeft);
        requestBody.put("right_url", urlRight);
        // 修复点：将 headers 和 body 封装到 HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//        //发送请求体
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                apiUrl,
//                requestEntity,
//                String.class
//        );
        try {
            // 打印最终请求体
            String jsonBody = new ObjectMapper().writeValueAsString(requestBody);
//            System.out.println("完整请求体：\n" + jsonBody);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers); // 使用String类型body
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            System.out.println("收到响应：" + response.getStatusCode());
            return new ResponseData(response.getBody(), true);
        } catch (Exception e) {
            System.err.println("请求失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData("请求算法服务失败", false);
        }

//        //获取返回结果
//        String r = response.getBody();
//        //将所有属性保存到case里面
//        return new ResponseData(r,true);
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
        // 创建存储分组文件的Map，键是患者ID，值是该患者的文件列表
        Map<String, List<MultipartFile>> fileGroups = new HashMap<>();
        // 定义文件名格式的正则表达式：
        // ^([a-zA-Z0-9_]+)      患者ID（字母/数字/下划线组成）
        // _(left|right)         左右眼标识
        // \.\\w+$               文件扩展名
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_]+)_([\\w\\u4e00-\\u9fff-]+)_(left|right)\\.\\w+$");
        System.out.println("文件数量：" + files.length);
        // 添加空文件数组检查
        if (files == null || files.length == 0) {
            throw new BusinessException(452,"上传文件列表不能为空");
        }
        // 遍历文件数组，根据文件名格式进行分组
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            // 用正则表达式匹配文件名格式
            Matcher matcher = pattern.matcher(fileName);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid filename format: " + fileName);
            }
            // 提取患者ID和类型，并将文件添加到对应的分组中
            String patientId = matcher.group(1);// 第1个括号匹配的内容（患者ID）
            String leftAndRight = matcher.group(3); // 第2个括号匹配的内容（left/right）
            // computeIfAbsent：如果不存在该患者ID的键，则创建新ArrayList,如果存在，则返回该键对应的值
            fileGroups.computeIfAbsent(patientId, k -> new ArrayList<>()).add(file);
        }

        return fileGroups;
    }

    @Override
    public PatientInfo selectPatientInfo(String patientId) {
        return patientInfoRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("患者信息不存在 ID：" + patientId));
    }
}

