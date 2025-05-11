package com.itshixun.industy.fundusexamination.Service.Impl;


import cn.hutool.core.bean.BeanUtil;
import com.itshixun.industy.fundusexamination.Service.UserService;
import com.itshixun.industy.fundusexamination.Utils.Md5Util;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.pojo.Enum.UserPermissionEnum;
import com.itshixun.industy.fundusexamination.pojo.InvitationCode;
import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.User;
import com.itshixun.industy.fundusexamination.pojo.dto.CreateUserByAdminDTO;
import com.itshixun.industy.fundusexamination.pojo.dto.UserDto;
import com.itshixun.industy.fundusexamination.pojo.dto.UserListDTO;
import com.itshixun.industy.fundusexamination.repository.InvitationCodeRepository;
import com.itshixun.industy.fundusexamination.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public PageBean<UserListDTO> getNonAdminUsers(Integer pageNum, Integer pageSize) {
        // 分页查询非管理员用户
        Page<User> nonAdminUsers = userRepository.findNonAdminUsers(PageRequest.of(pageNum-1, pageSize));
        // 将 User 转换为 UserListDTO
        return convertToPageBean(nonAdminUsers);

    }

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
