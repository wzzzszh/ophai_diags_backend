package com.itshixun.industy.fundusexamination.Service.Impl;


import com.itshixun.industy.fundusexamination.Service.PatientService;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import com.itshixun.industy.fundusexamination.pojo.dto.patientDto;
import com.itshixun.industy.fundusexamination.pojo.dto.patientLibDto;
import com.itshixun.industy.fundusexamination.repository.PatientInfoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientInfoRepository patientInfoRepository;
    @Override
    public PageBean<patientLibDto>
    getPatientListByPage(Integer pageNum,
                         Integer pageSize,
                         Integer age,
                         String name,
                         String patientId,
                         Integer gender) {

        // 实现分页查询逻辑
        // 根据条件查询数据库并返回分页结果
        // 1. 创建 Pageable 参数（PageRequest 是 Pageable 的子类）
        // 确保 pageNum 和 pageSize 不为 null
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("页码和每页数量不能为空");
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);

        // 2. 调用数据库查询方法，传入 Pageable 参数
        Page<PatientInfo> pageResult = patientInfoRepository.findPatientsByCriteria(
                pageable, age, name, patientId, gender);

        // 3. 处理查询结果，将其转换为 Page<patientLibDto>
        // 这里需要根据实际情况进行转换，可能需要自定义转换逻辑
        // 转换逻辑调整为适配实际PageBean结构
        List<patientLibDto> dtoList = pageResult.getContent().stream()
                .map(patient -> new patientLibDto(
                        patient.getPatientId(),
                        patient.getName(),
                        patient.getAge(),
                        patient.getGender(),
                        patient.getCreateDate()))
                .collect(Collectors.toList());
        return new PageBean<>(pageResult.getTotalElements(), dtoList);

    }

    @Override
    public PageBean<patientLibDto> selectPatientListByPageById(
            Integer pageNum,
            Integer pageSize,
            String patientId) {
        // 实现分页查询逻辑
        // 根据条件查询数据库并返回分页结果
        // 1. 创建 Pageable 参数（PageRequest 是 Pageable 的子类）
        // 确保 pageNum 和 pageSize 不为 null
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("页码和每页数量不能为空");
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        // 2. 调用数据库查询方法，传入 Pageable 参数
        Page<PatientInfo> pageResult = patientInfoRepository.findPatientsByPatientId(
                 patientId, pageable);
        // 3. 处理查询结果，将其转换为 Page<patientLibDto>
        // 这里需要根据实际情况进行转换，可能需要自定义转换逻辑
        // 转换逻辑调整为适配实际PageBean结构
        // 3. 处理查询结果，将其转换为 Page<patientLibDto>
        // 这里需要根据实际情况进行转换，可能需要自定义转换逻辑
        // 转换逻辑调整为适配实际PageBean结构
        List<patientLibDto> dtoList = pageResult.getContent().stream()
                .map(patient -> new patientLibDto(
                        patient.getPatientId(),
                        patient.getName(),
                        patient.getAge(),
                        patient.getGender(),
                        patient.getCreateDate()))
                .collect(Collectors.toList());
        return new PageBean<>(pageResult.getTotalElements(), dtoList);
    }

    @Override
    public PageBean<patientLibDto> selectPatientListByPageByName(Integer pageNum, Integer pageSize, String name) {
        // 实现分页查询逻辑
        // 根据条件查询数据库并返回分页结果
        // 1. 创建 Pageable 参数（PageRequest 是 Pageable 的子类）
        // 确保 pageNum 和 pageSize 不为 null
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("页码和每页数量不能为空");
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        //quanbu11
        if(name.equals("") ){
            name = null;
        }
        // 2. 调用数据库查询方法，传入 Pageable 参数
        Page<PatientInfo> pageResult = patientInfoRepository.findPatientsByName(
                name, pageable);
        // 3. 处理查询结果，将其转换为 Page<patientLibDto>
        // 这里需要根据实际情况进行转换，可能需要自定义转换逻辑
        // 转换逻辑调整为适配实际PageBean结构
        // 3. 处理查询结果，将其转换为 Page<patientLibDto>
        // 这里需要根据实际情况进行转换，可能需要自定义转换逻辑
        // 转换逻辑调整为适配实际PageBean结构
        List<patientLibDto> dtoList = pageResult.getContent().stream()
                .map(patient -> new patientLibDto(
                        patient.getPatientId(),
                        patient.getName(),
                        patient.getAge(),
                        patient.getGender(),
                        patient.getCreateDate()))
                .collect(Collectors.toList());
        return new PageBean<>(pageResult.getTotalElements(), dtoList);

    }

    @Override
    public PatientInfo updatePatientInfo(patientDto patientDto) {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientId(patientDto.getPatientId());
        patientInfo.setName(patientDto.getName());
        patientInfo.setAge(patientDto.getAge());
        patientInfo.setGender(patientDto.getGender());
        LocalDateTime currentDateTime = LocalDateTime.now();
        patientInfo.setCreateDate(currentDateTime);
        patientInfo.setIsDelete(0);
        patientInfo = patientInfoRepository.save(patientInfo);

        return patientInfo;
    }

    @Override
    public boolean delete(String patientId) {

        if(patientInfoRepository.deleteByIdO(patientId) == 1){
            return true;
        }
        return false;
    }

    @Override
    public patientDto add(PatientInfo patientInfo) {
        patientInfo.setIsDelete(0);
        PatientInfo save = patientInfoRepository.save(patientInfo);
        patientDto patientDto = new patientDto();
        BeanUtils.copyProperties(save, patientDto);
        return patientDto;
    }
}
