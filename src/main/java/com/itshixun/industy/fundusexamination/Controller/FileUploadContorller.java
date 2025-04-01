package com.itshixun.industy.fundusexamination.Controller;

import com.itshixun.industy.fundusexamination.Utils.AliOssUtil;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadContorller {

    @PostMapping("/api/upload")
    public ResponseMessage<String> fileUpload(MultipartFile file) throws Exception {
        //
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }
        //保证文件名唯一
        String filename = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));

        String url = AliOssUtil.uploadFile(filename, file.getInputStream());
        return ResponseMessage.success(url);
    }
}