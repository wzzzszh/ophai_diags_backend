package com.itshixun.industy.fundusexamination.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * 用户DTO
 */

public class UserDto {
    // 用户ID
    private Integer userId;
    // 用户名
    @NotBlank(message = "用户名不能为空")
    private String userName;
    // SHA256加密密码
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, message = "密码长度不能少于6位")
    private String passwordHash;
    // 邮箱
    @Email(message = "邮箱格式不正确")
    private String email;
    // 手机号
    private String phone;
    // 真实姓名
    private String realName;
    // 性别
    // 0: 女
    // 1: 男
    private int gender;
    // 年龄
    private int age;
    // 所在医院
    private String hospital;
    // 职位
    private String position;
    // 头像URL
    private String avatarUrl;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}