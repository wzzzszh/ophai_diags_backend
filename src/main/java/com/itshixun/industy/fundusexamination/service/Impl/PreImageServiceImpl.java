package com.itshixun.industy.fundusexamination.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itshixun.industy.fundusexamination.domain.dto.*;
import com.itshixun.industy.fundusexamination.domain.httpEnity.ResponseData;
import com.itshixun.industy.fundusexamination.domain.po.Case;
import com.itshixun.industy.fundusexamination.domain.po.OriginImageData;
import com.itshixun.industy.fundusexamination.domain.po.PageBean;
import com.itshixun.industy.fundusexamination.domain.po.PatientInfo;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.repository.CaseRepository;
import com.itshixun.industy.fundusexamination.repository.PatientInfoRepository;
import com.itshixun.industy.fundusexamination.repository.PreImageRepository;
import com.itshixun.industy.fundusexamination.service.CaseService;
import com.itshixun.industy.fundusexamination.service.PreImageService;
import com.itshixun.industy.fundusexamination.utils.AliOssUtil;
import com.itshixun.industy.fundusexamination.utils.RabbitMQ.ImageProcessMessage;
import com.itshixun.industy.fundusexamination.utils.RabbitMQ.RabbitMQConfig;
import com.itshixun.industy.fundusexamination.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class PreImageServiceImpl implements PreImageService {
    @Autowired
    private PreImageRepository preImageRepository;
    @Autowired
    private PatientInfoRepository patientInfoRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CaseService caseService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private CaseRepository caseRepository;
    // 初始化 ObjectMapper
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Case saveAndDiag(CaseDTO caseDto) {
        return null;
    }

    @Override
    public ResponseData sendUrltoP(int age ,int gender ,String name, String caseId, String urlLeft, String urlRight) {
        String apiUrl =
                "https://javelin-obliging-physically.ngrok-free.app/api/process-images/";
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
        try {
            // 打印最终请求体
            String jsonBody = new ObjectMapper().writeValueAsString(requestBody);


            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers); // 使用String类型body
            //发送请求到ai
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            System.out.println("病例ID为"+caseId+"的病例"
                    +"收到响应：" + response.getStatusCode());
            return new ResponseData(response.getBody(), true);
        } catch (Exception e) {
            System.err.println("请求失败：" + e.getMessage());
            e.printStackTrace();
            throw new BusinessException(453,"请求Ai失败");
        }
    }

    @Override
    public Map<String, String> saveOSS(String originalFileName1, String originalFileName2, InputStream inputStream1, InputStream inputStream2) {
        return Map.of();
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
                throw new IllegalArgumentException("不合法文件命名格式 " + fileName);
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
                .orElseThrow(() -> new BusinessException(422,"上传图像中有患者信息不存在ID为：" + patientId));
    }

    @Override
    public void saveAndProcess(MultipartFile[] files) throws IOException {
        //1.参数验证，并且用map来接收所有的文件
        // 1.1添加空文件数组检查
        if (files == null || files.length == 0) {
            throw new BusinessException(452,"上传文件列表不能为空");
        }
        Map<String, List<MultipartFile>> mapFiles;
        // 1.2 添加文件格式检查
        try {
            mapFiles = pattern(files);
        } catch (Exception e) {
            throw new BusinessException(453,"文件格式出现问题");
        }
        //2. 循环存储文件到病例
        for (Map.Entry<String, List<MultipartFile>> entry : mapFiles.entrySet()) {
            //2.1 获取键值对，并且循环接收pictures
            String patientId = entry.getKey();
            List<MultipartFile> pictures = entry.getValue();
            // 2.1.1检查 pictures 列表长度是否小于 2
            if (pictures.size() < 2) {
                throw new BusinessException(454, "患者 ID 为 " + patientId + " 的文件数量少于 2 个，请确保上传左右眼图片");
            }
            //2.2 判断patientId是否存在patient
            if(!caseService.isPatientExist(patientId)){
//                return ResponseMessage.allError(415,patientId + "病人不存在，请先添加病人信息" );
                throw new BusinessException(415,patientId + "病人不存在，请先添加病人信息" );
            }
            //2.3 新建case，保存该patientId到该病例的基本信息里面，初始化OriginImage，得到返回的caseId
            CaseDTO caseDto = new CaseDTO();
            //2.3.1 设置责任医生的姓名
            Map<String,Object> map = ThreadLocalUtil.get();
            String responsibleDoctor = (String) map.get("userName");
            PatientInfo patientInfo;
            //2.3.2获取患者信息注入病例
            patientInfo = selectPatientInfo(patientId);
            //2.3.2.1为dto注入patientInfo，originImageData，diagStatus，responsibleDoctor
            caseDto.setPatientInfo(patientInfo);
            caseDto.setOriginImageData(new OriginImageData());
            caseDto.setDiagStatus(0);
            caseDto.setResponsibleDoctor(responsibleDoctor);
            //2.3.2.2 获取保存后的实体
            Case caseNew = caseService.add(caseDto);
            //2.3.3 把caseId赋值到dto
            BeanUtils.copyProperties(caseNew, caseDto);
            String caseId = caseNew.getCaseId();
            //2.3.4 初始化图片URL
            String urlLeft = null;
            String urlRight = null;
            //2.3.5 准备患者信息
            String patientName = patientInfo.getName();
            int patientAge = patientInfo.getAge();
            int patientGender = patientInfo.getGender();
            //2.3.6据caseId重命名图片文件
            // 2.3.6.1循环处理图片文件
            for (MultipartFile file : pictures) {
                String originalName = file.getOriginalFilename();
                //2.3.6.2获取后缀
                String fileExtension = originalName.substring(originalName.lastIndexOf("."));

                // 2.3.6.3根据左右眼构建新的文件名
                String newFilename;
                if (originalName.contains("left")) {
                    newFilename = caseId + "_left" + fileExtension;
                } else if (originalName.contains("right")) {
                    newFilename = caseId + "_right" + fileExtension;
                } else {
                    continue; // 跳过不符合命名规则的文件
                }

                // 2.3.6.4上传到OSS
                String url = aliOssUtil.uploadFile(newFilename, file.getInputStream());
                System.out.println("oss存储的url"+url);
                // 2.3.6.5保存URL到对应变量
                if (originalName.contains("left")) {
                    urlLeft = url;
                } else {
                    urlRight = url;
                }
            }

            // 2.4.保存图片URL到病例
            caseDto.getOriginImageData().setLeftImage(urlLeft);
            caseDto.getOriginImageData().setRightImage(urlRight);
            caseService.update(caseDto);
            log.info("图片url保存成功,下面是消息队列！");
            // 2.5.发送消息到队列

            try {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.IMAGE_PROCESS_EXCHANGE,  // 使用交换机名称
                        RabbitMQConfig.ROUTING_KEY,             // 使用路由键
                        new ImageProcessMessage(
                                // 使用 MessageBuilder 构建消息
                                caseId,
                                urlLeft,
                                urlRight,
                                patientName,
                                patientAge,
                                patientGender
                        ),
                        message -> {
                            message.getMessageProperties().setContentType("application/json");
                            return message;
                        }
                );
            } catch (AmqpException e) {
                log.error("消息发送失败: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public PageBean<ImageDTO> SelectImageByPage(
            Integer pageNum, Integer pageSize,
            Integer diagStatus, String[] diseaseName,
            Integer gender,
            Integer startAge, Integer endAge,
            LocalDateTime startDate, LocalDateTime endDate) {
        //1.分页的默认值的设置
        if (diagStatus != null && diagStatus == -1) {
            diagStatus = null;
        }

        if ("全部".equals(diseaseName[0])) {
            diseaseName = null;
        }
        if (gender != null && gender == -1) {
            gender = null;
        }
        if (startAge != null && startAge == -1) {
            startAge = null;
        }
        if (endAge != null && endAge == -1) {
            endAge = null;
        }
        LocalDateTime specialDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        if(startDate.isEqual(specialDate)){
            startDate = null;
        }

        if (endDate != null && endDate.isEqual(specialDate)) {
            endDate = null;
        }
        //2.分页参数的设置
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("页码和每页数量不能为空");
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        //3.将diseaseName转换成Json字符串
        String diseaseNameJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 移除元素中的双引号（如果前端已经携带）
            if (diseaseName != null) {
                diseaseName = Arrays.stream(diseaseName)
                        .map(s -> s.replace("\"", "")) // 新增：去除每个疾病名称的双引号
                        .toArray(String[]::new);
            }
            diseaseNameJson = diseaseName != null ?
                    objectMapper.writeValueAsString(diseaseName) : null;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("疾病名称数组转换失败", e);
        }
        if(diseaseNameJson!=null) {
            //去除掉diseaseNameJson字段的[
            diseaseNameJson = diseaseNameJson.replace("[", "").replace("]", "");
        }// 去掉所有左方括号
        //4.调用repository的方法
        Page<Case> p = caseRepository.selectImageByPage(
                diagStatus, diseaseNameJson,
                gender, startAge, endAge,
                startDate, endDate, pageable);

        //5.将Page<Case>转换成PageBean<ImageDTO>
        PageBean<ImageDTO> p2 = convertToPageBean(p);

        return p2;
    }

    @Override
    public void batchExportImage(Integer pageNum, Integer pageSize,
                                 Integer diagStatus, String[] diseaseName,
                                 Integer gender,
                                 Integer startAge, Integer endAge,
                                 LocalDateTime startDate, LocalDateTime endDate,
                                 ZipOutputStream zipOut) {
        //1.分页的默认值的设置
        if (diagStatus != null && diagStatus == -1) {
            diagStatus = null;
        }
        if ("全部".equals(diseaseName[0])) {
            diseaseName = null;
        }
        if (gender != null && gender == -1) {
            gender = null;
        }
        if (startAge != null && startAge == -1) {
            startAge = null;
        }
        if (endAge != null && endAge == -1) {
            endAge = null;
        }
        //2.分页参数的设置
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("页码和每页数量不能为空");
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        //3.将diseaseName转换成Json字符串
        String diseaseNameJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 移除元素中的双引号（如果前端已经携带）
            if (diseaseName != null) {
                diseaseName = Arrays.stream(diseaseName)
                        .map(s -> s.replace("\"", "")) // 新增：去除每个疾病名称的双引号
                        .toArray(String[]::new);
            }
            diseaseNameJson = diseaseName != null ?
                    objectMapper.writeValueAsString(diseaseName) : null;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("疾病名称数组转换失败", e);
        }
        //4.调用repository的方法
        Page<Case> p = caseRepository.selectImageByPage(
                diagStatus, diseaseNameJson,
                gender, startAge, endAge,
                startDate, endDate, pageable);
        //5.将Page<Case>转换成PageBean<ImageDTO>
        PageBean<ImageDTO> p2 = convertToPageBean(p);
        //6.获取PageBean<ImageDTO>中的items
        List<ImageDTO> items = p2.getItems();
        //7.循环遍历items，获取每个ImageDTO中的leftImage和rightImage
        for (ImageDTO imageDTO : items) {
            String caseId = imageDTO.getCaseId();
            // 7.1处理原始图像
            OriginImageData origin = imageDTO.getOriginImageData();
            if (origin != null) {
                String leftImage = origin.getLeftImage();
                String rightImage = origin.getRightImage();
                //7.1.2.调用AliOssUtil的方法，将leftImage和rightImage下载到本地
                // 写入原始左眼图片
                writeImageToZip(zipOut, caseId, leftImage, leftImage.split("/")[4]);
                // 写入原始右眼图片
                writeImageToZip(zipOut, caseId, rightImage, rightImage.split("/")[4]);
            }
            // 8.处理 aiCaseInfo 中的图像
            String aiCaseInfoStr = imageDTO.getAiCaseInfo();
            System.out.println("原始 JSON 字符串内容：\n" + aiCaseInfoStr);
            if (aiCaseInfoStr != null) {
                try {
                    ApiResponseDTO apiResponseDTO = objectMapper.readValue(aiCaseInfoStr, ApiResponseDTO.class);
                    AICaseInfoDTO aiCaseInfo = apiResponseDTO.getAiCaseInfo();
                    System.out.println(aiCaseInfo+"aiCaseInfo");
                    processAICaseInfoImages(aiCaseInfo, caseId, zipOut);
                } catch (Exception e) {
                    // 处理异常，例如记录日志
                    e.printStackTrace();
                    log.error("处理aiCaseInfo时出错: {}", e.getMessage());
                }
            }

        }
    }


    /**
     * 导出excelZIP
     **/
    @Override
    public void exportDataToExcel(
            Integer pageNum, Integer pageSize,
            Integer diagStatus, String[] diseaseName,
            Integer gender,
            Integer startAge, Integer endAge,
            LocalDateTime startDate, LocalDateTime endDate,
            OutputStream out) {

        // 参数处理
        if (diagStatus != null && diagStatus == -1) {
            diagStatus = null;
        }
        if (diseaseName != null && "全部".equals(diseaseName[0])) {
            diseaseName = null;
        }
        if (gender != null && gender == -1) {
            gender = null;
        }
        if (startAge != null && startAge == -1) {
            startAge = null;
        }
        if (endAge != null && endAge == -1) {
            endAge = null;
        }
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("页码和每页数量不能为空");
        }

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        // 疾病名 JSON 处理
        String diseaseNameJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (diseaseName != null) {
                diseaseName = Arrays.stream(diseaseName)
                        .map(s -> s.replace("\"", ""))
                        .toArray(String[]::new);
            }
            diseaseNameJson = diseaseName != null ?
                    objectMapper.writeValueAsString(diseaseName) : null;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("疾病名称数组转换失败", e);
        }

        // 查询数据
        Page<Case> p = caseRepository.selectImageByPage(
                diagStatus, diseaseNameJson,
                gender, startAge, endAge,
                startDate, endDate, pageable);

        PageBean<ImageDTO> p2 = convertToPageBean(p);
        List<ImageDTO> items = p2.getItems();
        List<ExcelDataDTO> excelDataList = new ArrayList<>();

        for (ImageDTO imageDTO : items) {
            String caseId = imageDTO.getCaseId();
            ExcelDataDTO excelData = parseAiInfo(imageDTO);
            excelData.setCaseId(caseId);
            excelDataList.add(excelData);
        }

        // 使用 EasyExcel 写入输出流
        try {
            EasyExcel.write(out, ExcelDataDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("病例数据")
                    .doWrite(excelDataList);
        } catch (Exception e) {
            throw new RuntimeException("Excel生成失败", e);
        }
    }



    // 处理 AI 案例信息中的各类图像
    private void processAICaseInfoImages(AICaseInfoDTO aiCaseInfo, String caseId, ZipOutputStream zipOut)
    {
        if (aiCaseInfo == null || aiCaseInfo.getImages() == null) {
            log.info("没有获取到AI诊断的字段信息");
            return;
        }

        // 处理 heatmaps 中的疾病图像
        HeatmapsDTO heatmaps = aiCaseInfo.getImages().getHeatmaps();
        if (heatmaps != null) {
            List<String> diseases = heatmaps.getContains();
            if (diseases != null) {
                for (String disease : diseases) {
                    processHeatmapUrls(heatmaps.getLeftDiseases(), disease, caseId, zipOut, "left");
                    processHeatmapUrls(heatmaps.getRightDiseases(), disease, caseId, zipOut, "right");
                }
            }
        }

        // 处理 vessels 图像
        VesselsDTO vessels = aiCaseInfo.getImages().getVessels();
        if (vessels != null) {
            log.info("vessels不为空");
            writeImageToZip(zipOut, caseId, vessels.getLeft(), getFileNameFromUrl(vessels.getLeft()));
            writeImageToZip(zipOut, caseId, vessels.getRight(), getFileNameFromUrl(vessels.getRight()));
        }

        // 处理 disks 图像
        DisksDTO disks = aiCaseInfo.getImages().getDisks();
        if (disks != null) {
            log.info("disks不为空");
            writeImageToZip(zipOut, caseId, disks.getLeft(), getFileNameFromUrl(disks.getLeft()));
            writeImageToZip(zipOut, caseId, disks.getRight(), getFileNameFromUrl(disks.getRight()));
        }

        // 处理 original 图像（如果与原始图像不同）
        OriginalDTO original = aiCaseInfo.getImages().getOriginal();
        if (original != null) {
            log.info("original不为空");
            writeImageToZip(zipOut, caseId, original.getLeft(), getFileNameFromUrl(original.getLeft()));
            writeImageToZip(zipOut, caseId, original.getRight(), getFileNameFromUrl(original.getRight()));
        }
    }

    // 处理单个 heatmap 的 URL 列表
    private void processHeatmapUrls(Map<String, List<String>> diseaseUrls, String disease, String caseId, ZipOutputStream zipOut, String eyeSide) {
        if (diseaseUrls != null) {
            List<String> urls = diseaseUrls.get(disease);
            if (urls != null) {
                for (String url : urls) {
                    String fileName = getFileNameFromUrl(url);
                    writeImageToZip(zipOut, caseId, url, fileName);
                }
            }
        }
    }

    // 从 URL 提取文件名
    private String getFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) return "unknown.jpg";
        int lastSlashIndex = url.lastIndexOf('/');
        return (lastSlashIndex != -1) ? url.substring(lastSlashIndex + 1) : url;
    }

    /**
     * 将单个图片流写入 ZIP 的指定文件夹
     */
    private void writeImageToZip(
            ZipOutputStream zipOut,
            String caseId,
            String imageOssPath,  // OSS 文件路径（如 "folder/image.jpg"）
            String fileName      // 写入 ZIP 后的文件名（如 "left.jpg"）
    ) {
        if (imageOssPath == null || imageOssPath.isEmpty()) {
            return; // 跳过空路径
        }

        try {
            // 1. 创建 ZIP 条目路径（格式：caseId/fileName）
            String entryPath = caseId + "/" + fileName;
            ZipEntry zipEntry = new ZipEntry(entryPath);
            zipOut.putNextEntry(zipEntry);

            // 2. 从 OSS 下载图片字节流
            try (InputStream imageStream = aliOssUtil.downloadFile(imageOssPath)) {
                // 3. 将字节流写入 ZIP（使用 Apache Commons IO 工具类）
                IOUtils.copy(imageStream, zipOut);
            } catch (Exception e) {
                log.error("OSS 文件下载失败: {}", imageOssPath, e);
            }

            // 4. 关闭当前条目
            zipOut.closeEntry();
        } catch (IOException e) {
            log.error("ZIP 写入失败: caseId={}, fileName={}", caseId, fileName, e);
        }
    }


    private PageBean<ImageDTO> convertToPageBean(Page<Case> casePage) {
        PageBean<ImageDTO> pb = new PageBean<>();
        pb.setTotal(casePage.getTotalElements()); // 总记录数
        pb.setItems(
                casePage.getContent() // 当前页数据
                        .stream()
                        .map(this::convertToDto) // 转换为 DTO
                        .collect(Collectors.toList())
        );
        return pb;
    }
    private ImageDTO convertToDto(Case caseEntity) {
        ImageDTO imageDTO = new ImageDTO();
        BeanUtils.copyProperties(caseEntity, imageDTO);
        OriginImageData originImageData = caseEntity.getOriginImageData();
        imageDTO.setOriginImageData(originImageData);
        return imageDTO;
    }

    private ExcelDataDTO parseAiInfo(ImageDTO imageDTO) {
        ExcelDataDTO data = new ExcelDataDTO();
        data.setCaseId(imageDTO.getCaseId()); // 保持caseId直接赋值
        try {
            // 直接获取整个AI信息的原始JSON字符串
            String rawJson = imageDTO.getAiCaseInfo();
            data.setPredictions(extractJsonField(rawJson, "message.predictions"));
            data.setSuggestions(extractJsonField(rawJson, "message.suggestions"));
            data.setDrugs(extractJsonField(rawJson, "message.drugs"));
            data.setRevisitTime(extractJsonField(rawJson, "message.revisit_time"));
            data.setReportHtml(extractJsonField(rawJson, "message.report_html"));
            data.setQrCode(extractJsonField(rawJson, "message.qr_code"));
        } catch (Exception e) {
            log.error("解析AI信息失败 caseId: {}", imageDTO.getCaseId(), e);
            data.setPredictions("[解析错误] " + e.getMessage());
        }
        return data;
    }
    // 通用JSON字段提取方法
    private String extractJsonField(String jsonStr, String jsonPath) {
        try {
            JsonNode root = new ObjectMapper().readTree(jsonStr);
            JsonNode node = root.at("/" + jsonPath.replace(".", "/"));
            return node.isMissingNode() ? "" : node.toString();
        } catch (JsonProcessingException e) {
            return "[字段提取错误]";
        }
    }
}

