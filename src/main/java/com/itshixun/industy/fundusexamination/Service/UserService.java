package com.itshixun.industy.fundusexamination.Service;

import com.itshixun.industy.fundusexamination.pojo.User;
import com.itshixun.industy.fundusexamination.pojo.dto.UserDto;

public interface UserService {
    /**
     * 增加用户
     * @param user
     */
    User add(UserDto user);

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
    User update(UserDto user);

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
    User findByUserName(UserDto user);
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
    User findByUserId(UserDto user);
}
