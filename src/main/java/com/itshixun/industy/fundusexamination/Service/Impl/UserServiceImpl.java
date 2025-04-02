package com.itshixun.industy.fundusexamination.Service.Impl;


import com.itshixun.industy.fundusexamination.Service.UserService;
import com.itshixun.industy.fundusexamination.Utils.Md5Util;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.pojo.User;
import com.itshixun.industy.fundusexamination.pojo.dto.UserDto;
import com.itshixun.industy.fundusexamination.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User add(UserDto user) {
        // 校验密码和确认密码是否一致
        if (!(user.getPasswordHash().equals(user.getConfirmPassword()))) {
            throw new IllegalArgumentException("密码与确认密码不一致");
        }
        // 1. 检查用户名是否重复
        if (userRepository.existsByUserName((user.getUserName())) ){
            throw new ValidationException("用户名已存在");
        }

        // 2. 检查身份证号是否重复
        if (userRepository.existsByIdNumber((user.getIdNumber()))) {
            throw new ValidationException("身份证号已存在");
        }
        User userPojo = new User();
        //复制到user实体类
        BeanUtils.copyProperties(user, userPojo);
        String transPassword = Md5Util.getMD5String(userPojo.getPasswordHash());
        userPojo.setPasswordHash(transPassword);
        return userRepository.save(userPojo);
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
        User userPojo = userRepository.findByUserId(user.getUserId());
        return userPojo;
    }
}
