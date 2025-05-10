package com.itshixun.industy.fundusexamination.Service.Impl;


import com.itshixun.industy.fundusexamination.Service.UserService;
import com.itshixun.industy.fundusexamination.Utils.Md5Util;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.pojo.InvitationCode;
import com.itshixun.industy.fundusexamination.pojo.User;
import com.itshixun.industy.fundusexamination.pojo.dto.CreateUserByAdminDTO;
import com.itshixun.industy.fundusexamination.pojo.dto.UserDto;
import com.itshixun.industy.fundusexamination.repository.InvitationCodeRepository;
import com.itshixun.industy.fundusexamination.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvitationCodeRepository invitationCodeRepository;

    @Override
    public User addAdmin(UserDto user) {
        // 校验密码和确认密码是否一致
        if (!(user.getPasswordHash().equals(user.getConfirmPassword()))) {
//            newUser.setUserName("密码与确认密码不一致001");
//            return newUser;
//            throw new IllegalArgumentException("密码与确认密码不一致");
            throw new BusinessException(406,"密码与确认密码不一致");
        }
        // 1. 检查用户名是否重复
        if (userRepository.existsByUserName((user.getUserName())) ){
//            newUser.setUserName("用户名已经存在001");
//            return newUser;
//            throw new ValidationException("用户名已存在");
            throw new BusinessException(407,"用户名已经存在");

        }

        // 2. 检查身份证号是否重复
        if (userRepository.existsByIdNumber((user.getIdNumber()))) {
//            newUser.setUserName("身份证号已存在001");
//            return newUser;
//            throw new ValidationException("身份证号已存在");
            throw new BusinessException(408,"身份证号已存在");
        }
        InvitationCode code = invitationCodeRepository.findByCode(user.getInvitationCode());
        if(code == null){
            throw new BusinessException(409,"邀请码不存在");
        }
        User userPojo = new User();
        //复制到user实体类
        BeanUtils.copyProperties(user, userPojo);
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

    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new IllegalStateException("用户不存在");
        });
    }

    @Override
    public User update(UserDto user) {

        User userPojo = new User();
        BeanUtils.copyProperties(user, userPojo);
        return userRepository.save(userPojo);

    }

    @Override
    public void delete(String userId) {
        userRepository.deleteById(userId);
    }


    //待修改
    @Override
    public User findByUserName(UserDto user) {
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
    public User findByUserId(UserDto user) {
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
}
