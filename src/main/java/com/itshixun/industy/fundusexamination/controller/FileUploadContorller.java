package com.itshixun.industy.fundusexamination.controller;

import com.itshixun.industy.fundusexamination.utils.AliOssUtil;
import com.itshixun.industy.fundusexamination.utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadContorller {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/api/upload")
    public ResponseMessage<String> fileUpload(MultipartFile file) throws Exception {
        //
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new BusinessException(418,"文件名不能为空");
        }
        //保证文件名唯一
        String filename = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));

        String url = aliOssUtil.uploadFile(filename, file.getInputStream());
        return ResponseMessage.success(url);
    }

}