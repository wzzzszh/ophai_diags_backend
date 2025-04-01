package com.itshixun.industy.fundusexamination.Controller;

import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.Service.PreImageService;
import com.itshixun.industy.fundusexamination.Utils.AliOssUtil;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.OriginImageData;
import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.ResponseData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

//保存图片信息并且进行ai诊断
@RestController
@RequestMapping("/api/preImage")
public class PreImageController {
    @Autowired
    public CaseService caseService;
    @Autowired
    public PreImageService preImageService;

    /**
     * 已经弃用
     * @status 弃用
     * @param caseDto
     * @param fileLeft
     * @param fileRight
     * @return
     * @throws Exception
     */
    @PostMapping("/saveAndProcesserror")
    public ResponseMessage<ResponseData> saveAndDiag(CaseDto caseDto,MultipartFile fileLeft,MultipartFile fileRight)throws Exception{
        //初步保存case
        Case caseNew = caseService.add(caseDto);
        //保留caseId
        String caseId = caseNew.getCaseId();
        //参数验证
        String originalFileName1 = fileLeft.getOriginalFilename();
        String originalFileName2 = fileRight.getOriginalFilename();
        //进一步参数验证并且保存图片到OSS
        Map<String, String> oriUrl = new HashMap<>();
        oriUrl = preImageService.saveOSS(
                originalFileName1,
                originalFileName2,
                fileLeft.getInputStream(),
                fileRight.getInputStream()
        );

        //提取hashmap到oriUrl
        String urlLeft = oriUrl.get("urlLeft");
        String urlRight = oriUrl.get("urlRight");

        //Service发送url给算法
        ResponseData allInfoFromP = preImageService.sendUrltoP(caseId, urlLeft, urlRight);
        //接受算法返回结果
        String info = allInfoFromP.getMessage();
        //保存图片信息还有诊断结果
        caseDto.getOriginImageData().setLeftImage(urlLeft);
        caseDto.getOriginImageData().setRightImage(urlRight);
        //更新病例信息
        caseService.add(caseDto);
        CaseDto caseDtoF = new CaseDto();
        //保存病例信息，ai诊断
//        Case caseNew = preImageService.saveAndDiag(caseDto);
        //返回给前端成功信息
        return ResponseMessage.success();

    }
    @PostMapping("/saveAndProcess")
    public ResponseMessage<ResponseData> savePreAndProcess(MultipartFile[] files)throws Exception{
        //1.参数验证，用map来接收所有的文件
        Map<String, List<MultipartFile>> mapFiles = preImageService.pattern(files);
        //2.循环接收mapFiles
        //迭代mapFiles
        for (Map.Entry<String, List<MultipartFile>> entry : mapFiles.entrySet()) {
            //获取键值对，并且循环接收pictures
            String patientId = entry.getKey();
            List<MultipartFile> pictures = entry.getValue();

            //判断patientId是否存在patient
            if(!caseService.isPatientExist(patientId)){
                return ResponseMessage.allError(415,patientId + "病人不存在，请先添加病人信息" );
            }

            //新建case，保存该patientId到该病例的基本信息里面，初始化OriginImage，得到返回的caseId
            CaseDto caseDto = new CaseDto();
            //设置责任医生的姓名
            Map<String,Object> map = ThreadLocalUtil.get();
            String responsibleDoctor = (String) map.get("userName");
            ThreadLocalUtil.remove();
            PatientInfo patientInfo = new PatientInfo();
            //获取患者信息注入病例
            patientInfo = preImageService.selectPatientInfo(patientId);
            caseDto.setPatientInfo(patientInfo);
            caseDto.setOriginImageData(new OriginImageData());
            caseDto.setDiagStatus(0);
            caseDto.setResponsibleDoctor(responsibleDoctor);
            Case caseNew = caseService.add(caseDto);
            //把caseId赋值到dto
            BeanUtils.copyProperties(caseNew, caseDto);

            String caseId = caseNew.getCaseId();
            // 初始化图片URL
            String urlLeft = null;
            String urlRight = null;
            //根据caseId重命名图片文件
            // 循环处理图片文件
            for (MultipartFile file : pictures) {
                String originalName = file.getOriginalFilename();
                //获取后缀
                String fileExtension = originalName.substring(originalName.lastIndexOf("."));

                // 构建新的文件名
                String newFilename;
                if (originalName.contains("left")) {
                    newFilename = caseId + "_left" + fileExtension;
                } else if (originalName.contains("right")) {
                    newFilename = caseId + "_right" + fileExtension;
                } else {
                    continue; // 跳过不符合命名规则的文件
                }

                // 上传到OSS
                String url = AliOssUtil.uploadFile(newFilename, file.getInputStream());

                // 保存URL到对应变量
                if (originalName.contains("left")) {
                    urlLeft = url;
                } else {
                    urlRight = url;
                }
            }

            // 保存图片URL到病例
            caseDto.getOriginImageData().setLeftImage(urlLeft);
            caseDto.getOriginImageData().setRightImage(urlRight);
            //改名后的文件发送到算法端
            ResponseData responseData = preImageService.sendUrltoP(caseId, urlLeft, urlRight);
            String fullJson = responseData.getMessage();
            //判断是否有图片
            if (!responseData.getSuccess()) {
                // 处理没有图片的情况
                caseNew.setDiagStatus(2);
                return ResponseMessage.allError(417,"ai诊断失败!!!请联系管理员");
            }
            // 直接存入case_info字段
            caseDto.setAiCaseInfo(fullJson);
            caseDto = caseService.update(caseDto);
            //

            // 修改后的正确方式
            Case managedCase = caseService.getCaseById(caseNew.getCaseId()); // 重新获取托管状态的实体
            managedCase.setAiCaseInfo(fullJson);
            BeanUtils.copyProperties(managedCase, caseDto);
            //设置正确诊断状态
            caseDto.setDiagStatus(1);
            caseService.update(caseDto);
        }

        return ResponseMessage.success("保存病例成功，请前往诊断页面",null);
    }
}
