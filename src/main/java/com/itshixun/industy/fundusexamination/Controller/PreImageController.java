package com.itshixun.industy.fundusexamination.Controller;

import com.itshixun.industy.fundusexamination.Service.PreImageService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/preimage")
public class PreImageController {
    //保存图片信息并且进行ai诊断
    @Autowired
    public PreImageService preImageService;
    @PostMapping("/save")
    public ResponseMessage<Boolean> saveAndDiag(CaseDto caseDto){
        //参数验证

        //保存图片信息到OSS

        //发送url给算法

        //接受算法返回结果

        //保存图片信息到url

        //保存病例信息，ai诊断
        Case caseNew = preImageService.saveAndDiag(caseDto);
        //返回给前端成功信息
        return ResponseMessage.success(caseNew != null);

    }
}
