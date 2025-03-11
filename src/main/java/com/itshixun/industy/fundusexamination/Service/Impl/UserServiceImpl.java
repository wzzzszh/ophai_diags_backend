package com.itshixun.industy.fundusexamination.Service.Impl;


import com.itshixun.industy.fundusexamination.Service.UserService;
import com.itshixun.industy.fundusexamination.Utils.Md5Util;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.pojo.User;
import com.itshixun.industy.fundusexamination.pojo.dto.UserDto;
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

    @Override
    public User add(UserDto user) {
        User userPojo = new User();

        BeanUtils.copyProperties(user, userPojo);
        String transPassword = Md5Util.getMD5String(userPojo.getPasswordHash());
        userPojo.setPasswordHash(transPassword);

        return userRepository.save(userPojo);


    }

    @Override
    public User getUser(Integer userId) {
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
    public void delete(Integer userId) {
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
        Integer userId = (Integer) map.get("userId");
        User userPojo = userRepository.findByUserId(userId);
        userPojo.setAvatarUrl(avatarUrl);
        userRepository.save(userPojo);

    }
}
