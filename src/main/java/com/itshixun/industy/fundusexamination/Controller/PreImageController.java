package com.itshixun.industy.fundusexamination.Controller;

import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.Service.PreImageService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.dto.ImageDTO;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.ResponseData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 图像管理
 *
 * @author 孙宗昊
 * @since 2025-04-19
 */
@Slf4j
@RestController
@RequestMapping("/api/preImage")
public class PreImageController {
    @Autowired
    public CaseService caseService;
    @Autowired
    public PreImageService preImageService;



//    @PostMapping("/saveAndProcess1")
//    public ResponseMessage<ResponseData> savePreAndProcess1(MultipartFile[] files)throws Exception{
//        //1.参数验证，用map来接收所有的文件
//        Map<String, List<MultipartFile>> mapFiles = preImageService.pattern(files);
//        //2.循环接收mapFiles
//        //迭代mapFiles
//        for (Map.Entry<String, List<MultipartFile>> entry : mapFiles.entrySet()) {
//            //获取键值对，并且循环接收pictures
//            String patientId = entry.getKey();
//            List<MultipartFile> pictures = entry.getValue();
//
//            //判断patientId是否存在patient
//            if(!caseService.isPatientExist(patientId)){
//                return ResponseMessage.allError(415,patientId + "病人不存在，请先添加病人信息" );
//            }
//
//            //新建case，保存该patientId到该病例的基本信息里面，初始化OriginImage，得到返回的caseId
//            CaseDto caseDto = new CaseDto();
//            //设置责任医生的姓名
//            Map<String,Object> map = ThreadLocalUtil.get();
//            String responsibleDoctor = (String) map.get("userName");
//            ThreadLocalUtil.remove();
//            PatientInfo patientInfo = new PatientInfo();
//            //获取患者信息注入病例
//            patientInfo = preImageService.selectPatientInfo(patientId);
//            caseDto.setPatientInfo(patientInfo);
//            caseDto.setOriginImageData(new OriginImageData());
//            caseDto.setDiagStatus(0);
//            caseDto.setResponsibleDoctor(responsibleDoctor);
//            Case caseNew = caseService.add(caseDto);
//            //把caseId赋值到dto
//            BeanUtils.copyProperties(caseNew, caseDto);
//
//            String caseId = caseNew.getCaseId();
//            // 初始化图片URL
//            String urlLeft = null;
//            String urlRight = null;
//
//            //准备患者信息
//            String patientName = patientInfo.getName();
//            int patientAge = patientInfo.getAge();
//            int patientGender = patientInfo.getGender();
//            //根据caseId重命名图片文件
//            // 循环处理图片文件
//            for (MultipartFile file : pictures) {
//                String originalName = file.getOriginalFilename();
//                //获取后缀
//                String fileExtension = originalName.substring(originalName.lastIndexOf("."));
//
//                // 构建新的文件名
//                String newFilename;
//                if (originalName.contains("left")) {
//                    newFilename = caseId + "_left" + fileExtension;
//                } else if (originalName.contains("right")) {
//                    newFilename = caseId + "_right" + fileExtension;
//                } else {
//                    continue; // 跳过不符合命名规则的文件
//                }
//
//                // 上传到OSS
//                String url = AliOssUtil.uploadFile(newFilename, file.getInputStream());
//
//                // 保存URL到对应变量
//                if (originalName.contains("left")) {
//                    urlLeft = url;
//                } else {
//                    urlRight = url;
//                }
//            }
//
//            // 保存图片URL到病例
//            caseDto.getOriginImageData().setLeftImage(urlLeft);
//            caseDto.getOriginImageData().setRightImage(urlRight);
//            //改名后的文件发送到算法端
//            ResponseData responseData = preImageService.sendUrltoP(patientAge,patientGender,patientName, caseId, urlLeft, urlRight);
//
//            String fullJson = responseData.getMessage();
//            //判断是否有图片
//            if (!responseData.getSuccess()) {
//                // 处理没有图片的情况
//                caseNew.setDiagStatus(2);
//                return ResponseMessage.allError(417,"ai诊断失败!!!请联系管理员");
//            }
//            // 直接存入case_info字段
//            caseDto.setAiCaseInfo(fullJson);
//            caseDto = caseService.update(caseDto);
//            //
//
//            // 修改后的正确方式
//            Case managedCase = caseService.getCaseById(caseNew.getCaseId()); // 重新获取托管状态的实体
//            managedCase.setAiCaseInfo(fullJson);
//            BeanUtils.copyProperties(managedCase, caseDto);
//            //设置正确诊断状态
//            caseDto.setDiagStatus(1);
//            caseService.update(caseDto);
//        }
//
//        return ResponseMessage.success("保存病例成功，请前往诊断页面",null);
//    }
    @PostMapping("/saveAndProcess")
    public ResponseMessage<ResponseData> savePreAndProcess(MultipartFile[] files)throws Exception{

        preImageService.saveAndProcess(files);
        return ResponseMessage.success("保存病例成功，请前往诊断页面",null);
    }
    /**
     * 分页查询图像库
     * @param pageNum
     * @param pageSize
     * @param diagStatus
     * @param diseaseName
     * @param gender
     * @param StartAge
     * @param EndAge
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/list")
    public ResponseMessage<PageBean<ImageDTO>> SelectImageByPage(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer diagStatus,
            @RequestParam(required = false) String diseaseName,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) Integer StartAge,
            @RequestParam(required = false) Integer EndAge,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ){
        PageBean<ImageDTO> p = null;
        try {
            String[] diseaseNameArray = diseaseName.split(",");
            p = preImageService.SelectImageByPage(
                    pageNum,
                    pageSize,
                    diagStatus,
                    diseaseNameArray,
                    gender,
                    StartAge,
                    EndAge,
                    startDate,
                    endDate);
        } catch (Exception e) {
            throw new BusinessException(600,"查询失败");
        }

        return ResponseMessage.success(p);


    }
    /**
     * 批量导出图像
     * @param pageNum
     * @param pageSize
     * @param diagStatus
     * @param diseaseName
     * @param gender
     * @param StartAge
     * @param EndAge
     * @param startDate
     * @param endDate
     * @param response
     */
    @PostMapping("/batchExportImage")
    public void batchExportImage(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer diagStatus,
            @RequestParam(required = false) String diseaseName,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) Integer StartAge,
            @RequestParam(required = false) Integer EndAge,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            HttpServletResponse response
    ) throws IOException {
        // 1. 处理 diseaseName 为数组
        String[] diseaseNameArray = diseaseName.split(",");

        // 2. 设置响应头
        response.setContentType("application/zip");
        String fileName = "images_" + System.currentTimeMillis() + ".zip";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 3. 创建 ZIP 流（try-with-resources 自动管理资源）
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            // 4. 调用 Service 层写入数据
            preImageService.batchExportImage(
                    pageNum,
                    pageSize,
                    diagStatus,
                    diseaseNameArray,
                    gender,
                    StartAge,
                    EndAge,
                    startDate,
                    endDate,
                    zipOut
            );
        } catch (Exception e) {
            // 5. 异常处理（确保响应未提交时才重置）
            if (!response.isCommitted()) {
                response.reset();
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.getWriter().write("导出失败: " + e.getMessage());
            } else {
                log.error("导出失败，但响应已提交", e);
            }
        }
    }
    /**
     * 批量导出Excel
     * @param pageNum
     * @param pageSize
     * @param diagStatus
     * @param diseaseName
     * @param gender
     * @param StartAge
     * @param EndAge
     * @param startDate
     * @param endDate
     * @param response
     */
//    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
    @PostMapping("/exportExcel")
// 也可用 @GetMapping，但若参数较多推荐 POST
    public void exportExcel(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer diagStatus,
            @RequestParam(required = false) String diseaseName,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) Integer StartAge, // 推荐小驼峰命名 StartAge -> startAge
            @RequestParam(required = false) Integer EndAge,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            HttpServletResponse response) throws IOException {
        // 0. 处理 diseaseName 为数组
        String[] diseaseNameArray = diseaseName.split(",");
        // 1. 设置响应头（强制 ZIP 格式）
        String zipName = "数据导出_" + LocalDate.now() + ".zip";
        String encodedZipName = URLEncoder.encode(zipName, "UTF-8").replaceAll("\\+", "%20");
        response.reset();
        response.setContentType("application/zip");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedZipName + "\"");
        // 2. 获取输出流
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            // 2. 生成 Excel 并写入 ZIP
            String excelName = "病例数据_" + LocalDate.now() + ".xlsx";
            ZipEntry zipEntry = new ZipEntry(excelName);
            zipOut.putNextEntry(zipEntry);

            // 3. 调用 Service 层生成 Excel 到 ZIP 流
            preImageService.exportDataToZip(
                    pageNum, pageSize, diagStatus, diseaseNameArray,
                    gender, StartAge, EndAge, startDate, endDate, zipOut
            );

            zipOut.closeEntry();
        } catch (Exception e) {
            if (!response.isCommitted()) {
                response.reset();
                response.setContentType("application/json");
                response.getWriter().write("{\"code\":500, \"msg\":\"导出失败: " + e.getMessage() + "\"}");
            } else {
                log.error("ZIP导出异常（响应已提交）", e);
            }
        }
}}
