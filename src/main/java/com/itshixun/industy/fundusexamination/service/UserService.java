package com.itshixun.industy.fundusexamination.service;

import com.itshixun.industy.fundusexamination.domain.dto.*;
import com.itshixun.industy.fundusexamination.domain.po.PageBean;
import com.itshixun.industy.fundusexamination.domain.po.User;
import com.itshixun.industy.fundusexamination.domain.vo.UserPatientVO;

public interface UserService {
    /**
     * 增加用户
     * @param user
     */
    User addAdmin(UserDTO user);

    /**
     * 查询用户
     * @param userId
     * @return
     */
    User getUser(String userId);
    /**
     * 更新用户
     * @param user
     * @return
     * */
    User update(String userId, UserDTO user);

    /**
     * 删除用户
     *
     * @param userId
     */
    void delete(String userId);

    /**
     * 通过用户名查询用户登录
     * @param user
     * @return
     */
    User findByUserName(UserDTO user);
    /**
     * 更新头像
     * @param avatarUrl
     * @return
     * */
    void updateAvatar(String avatarUrl);
    /**
     * 通过用户ID查询用户
     * @param user
     * @return
     */
    User findByUserId(LoginDTO user);

    User addOther(CreateUserByAdminDTO user);

    PageBean<UserListDTO> getNonAdminUsers(Integer pageNum, Integer pageSize);

    UserListDTO updatePermission(String userId, Integer permission);

    UserPatientVO patientRegister(PatientRegisterDTO patientDTO);

    User findByIdCard(LoginDTO user);
}
