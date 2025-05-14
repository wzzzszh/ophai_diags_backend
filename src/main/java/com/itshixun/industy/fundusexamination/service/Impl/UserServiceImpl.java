package com.itshixun.industy.fundusexamination.service.Impl;


import cn.hutool.core.bean.BeanUtil;
import com.itshixun.industy.fundusexamination.annotation.AddCache;
import com.itshixun.industy.fundusexamination.annotation.DelCache;
import com.itshixun.industy.fundusexamination.domain.dto.*;
import com.itshixun.industy.fundusexamination.domain.po.PatientInfo;
import com.itshixun.industy.fundusexamination.domain.vo.UserPatientVO;
import com.itshixun.industy.fundusexamination.repository.PatientInfoRepository;
import com.itshixun.industy.fundusexamination.service.UserService;
import com.itshixun.industy.fundusexamination.utils.Md5Util;
import com.itshixun.industy.fundusexamination.utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.domain.enums.UserPermissionEnum;
import com.itshixun.industy.fundusexamination.domain.po.InvitationCode;
import com.itshixun.industy.fundusexamination.domain.po.PageBean;
import com.itshixun.industy.fundusexamination.domain.po.User;
import com.itshixun.industy.fundusexamination.repository.InvitationCodeRepository;
import com.itshixun.industy.fundusexamination.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvitationCodeRepository invitationCodeRepository;

    @Autowired
    PatientInfoRepository patientInfoRepository;

    @Override
    public User addAdmin(UserDTO user) {
        // 校验密码和确认密码是否一致
        if (!(user.getPasswordHash().equals(user.getConfirmPassword()))) {

            throw new BusinessException(406,"密码与确认密码不一致");
        }
        // 1. 检查用户名是否重复
        if (userRepository.existsByUserName((user.getUserName())) ){

            throw new BusinessException(407,"用户名已经存在");

        }

        // 2. 检查身份证号是否重复
        if (userRepository.existsByIdNumber((user.getIdNumber()))) {

            throw new BusinessException(408,"身份证号已存在");
        }
        InvitationCode code = invitationCodeRepository.findByCode(user.getInvitationCode());
        if(code == null){
            throw new BusinessException(409,"邀请码不存在");
        }
        User userPojo = new User();
        //3.复制到user实体类
        BeanUtils.copyProperties(user, userPojo);
        userPojo.setPermission(4);
        String transPassword = Md5Util.getMD5String(userPojo.getPasswordHash());
        userPojo.setPasswordHash(transPassword);
        User saveUser = null;
        try {
            saveUser = userRepository.save(userPojo);
            invitationCodeRepository.delete(code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return saveUser;
    }
    @AddCache(prefix = "user")
    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
    }
    @DelCache(prefix = "user")
    @Override
    public User update(String userId, UserDTO user) {

        User userPojo = new User();
        BeanUtils.copyProperties(user, userPojo);
        return userRepository.save(userPojo);

    }
    @DelCache(prefix = "user")
    @Override
    public void delete(String userId) {
        User user = getUser(userId);
        if(user.getPermission() == UserPermissionEnum.ADMIN.getCode()){
            throw new BusinessException(409,"无法删除管理员账号");
        }
        userRepository.deleteById(userId);
    }


    //待修改
    @Override
    public User findByUserName(UserDTO user) {
        User userPojo = userRepository.findByUserName(user.getUserName());
        System.out.println("service层查询用户"+userPojo);
        return userPojo;
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map = ThreadLocalUtil.get();
        String userId = (String) map.get("userId");
        User userPojo = userRepository.findByUserId(userId);
        userPojo.setAvatarUrl(avatarUrl);
        userRepository.save(userPojo);

    }

    @Override
    public User findByUserId(LoginDTO user) {
        return userRepository.findByUserId(user.getUserId());
    }

    @Override
    public User addOther(CreateUserByAdminDTO user) {

        // 1. 检查用户名是否重复
        if (userRepository.existsByUserName((user.getUserName())) ){

            throw new BusinessException(407,"用户名已经存在");

        }

        // 2. 检查身份证号是否重复
        if (userRepository.existsByIdNumber((user.getIdNumber()))) {

            throw new BusinessException(408,"身份证号已存在");
        }
        Map<String,Object> map = ThreadLocalUtil.get();
        String userId = (String) map.get("userId");
        User thisUser = getUser(userId);

        User userPojo = new User();

        //复制到user实体类
        BeanUtils.copyProperties(user, userPojo);
        userPojo.setHospital(thisUser.getHospital());
        String transPassword = Md5Util.getMD5String(userPojo.getPasswordHash());
        userPojo.setPasswordHash(transPassword);
        if(userPojo.getPermission() == 4){
            throw new BusinessException(409,"无法注册管理员账号");
        }
        return userRepository.save(userPojo);
    }

    @Override
    public PageBean<UserListDTO> getNonAdminUsers(Integer pageNum, Integer pageSize) {
        // 分页查询非管理员用户
        Page<User> nonAdminUsers = userRepository.findNonAdminUsers(PageRequest.of(pageNum-1, pageSize));
        // 将 User 转换为 UserListDTO
        return convertToPageBean(nonAdminUsers);

    }
    @DelCache(prefix = "user")
    @Override
    public UserListDTO updatePermission(String userId, Integer permission) {
        if(UserPermissionEnum.ADMIN.getCode() == permission){
            throw new BusinessException(409,"无法修改为管理员权限");
        }
        User user = userRepository.findByUserId(userId);
        if(user != null){
            if(user.getPermission() == UserPermissionEnum.ADMIN.getCode()){
                throw new BusinessException(409,"无法修改管理员权限");
            }
            user.setPermission(permission);
            return BeanUtil.copyProperties(userRepository.save(user), UserListDTO.class);
        }
        throw new BusinessException(409,"用户不存在");
    }

    @Override
    public UserPatientVO patientRegister(PatientRegisterDTO patientDTO) {
        PatientInfo patient = patientInfoRepository.findByIdCard(patientDTO.getIdCard());
        User saveUser = null;
        if (patient != null){
            patient.setAddress(patientDTO.getAddress());
            patient.setEmergencyContact(patientDTO.getEmergencyContact());
            patient.setMedicalCard(patientDTO.getMedicalCard());
            patient.setUpdateDate(LocalDateTime.now());

            patientInfoRepository.save(patient);

            User user = new User();
            user.setUserName(patientDTO.getName());
            user.setIdNumber(patientDTO.getIdCard());
            user.setPhone(patientDTO.getPhone());
            user.setRealName(patientDTO.getName());
            user.setGender(patient.getGender());
            user.setAge(patient.getAge());
            user.setPermission(UserPermissionEnum.PATIENT.getCode());
            user.setPasswordHash(Md5Util.getMD5String(patientDTO.getPassword()));
            user.setPatientInfo(patient);
            user.setCreateDate(LocalDateTime.now());
            saveUser = userRepository.save(user);

        } else {
            patient = new PatientInfo();
            patient.setIdCard(patientDTO.getIdCard());
            patient.setName(patientDTO.getName());
            // 通过身份证号判断性别
            if (patientDTO.getIdCard().length() == 18) {
                char genderChar = patientDTO.getIdCard().charAt(16);
                if (genderChar % 2 == 0) {
                    patient.setGender(0);
                } else {
                    patient.setGender(1);
                }
            }
            // 通过身份证号判断年龄
            if (patientDTO.getIdCard().length() == 18) {
                String birthYear = patientDTO.getIdCard().substring(6, 10);
                int currentYear = LocalDateTime.now().getYear();
                int age = currentYear - Integer.parseInt(birthYear);
                patient.setAge(age);
            }
            patient.setAddress(patientDTO.getAddress());
            patient.setEmergencyContact(patientDTO.getEmergencyContact());
            patient.setMedicalCard(patientDTO.getMedicalCard());
            patient.setCreateDate(LocalDateTime.now());
            patientInfoRepository.save(patient);
            User user = new User();
            user.setUserName(patientDTO.getName());
            user.setIdNumber(patientDTO.getIdCard());
            user.setPhone(patientDTO.getPhone());
            user.setPermission(UserPermissionEnum.PATIENT.getCode());
            user.setCreateDate(LocalDateTime.now());
            user.setRealName(patientDTO.getName());
            user.setPasswordHash(Md5Util.getMD5String(patientDTO.getPassword()));
            user.setPatientInfo(patient);
            user.setAge(patient.getAge());
            user.setGender(patient.getGender());

            saveUser = userRepository.save(user);

        }
        return getUserPatientVO(saveUser, patient);
    }

    @Override
    public User findByIdCard(LoginDTO user) {
        return userRepository.findByIdCard(user.getUserId());
    }

    private static UserPatientVO getUserPatientVO(User saveUser, PatientInfo patient) {
        UserPatientVO userPatientVO = new UserPatientVO();
        userPatientVO.setUserId(saveUser.getUserId());
        userPatientVO.setIdCard(patient.getIdCard());
        userPatientVO.setMedicalCard(patient.getMedicalCard());
        userPatientVO.setPhone(patient.getPhone());
        userPatientVO.setAddress(patient.getAddress());
        userPatientVO.setEmergencyContact(patient.getEmergencyContact());
        userPatientVO.setAge(patient.getAge());
        userPatientVO.setGender(patient.getGender());
        userPatientVO.setName(patient.getName());
        return userPatientVO;
    }


    private PageBean<UserListDTO> convertToPageBean(Page<User> casePage) {
        PageBean<UserListDTO> pb = new PageBean<>();
        pb.setTotal(casePage.getTotalElements()); // 总记录数
        pb.setItems(
                casePage.getContent() // 当前页数据
                        .stream()
                        .map(this::convertToDto) // 转换为 DTO
                        .collect(Collectors.toList())
        );
        return pb;
    }

    private UserListDTO convertToDto(User userEntity) {
        UserListDTO dto = new UserListDTO();
        BeanUtils.copyProperties(userEntity, dto);
        //将实体类里面的diseaseNameJson转换成String[]类型
        //再存储到dto里面的diseaseName字段
        return dto;
    }
}
