package com.itshixun.industy.fundusexamination.pojo;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 *
 * @author 10169
 * @Description 用户表
 * @Date 2025/4/18 15:40
 *
 */
@Table(name="tb_user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 用户ID
    @Column(name = "user_id")
    private Integer userId;
    // 用户名
    @Column(name = "user_name")
    private String userName;
    // SHA256加密密码
    @Column(name = "password_hash")
    private String passwordHash;
    // 邮箱
    @Column(name = "email")
    private String email;
    // 手机号
    @Column(name = "phone")
    private String phone;
    // 真实姓名
    @Column(name = "real_name")
    private String realName;
    // 性别
    // 0: 女
    // 1: 男
    @Column(name = "gender")
    private int gender;
    // 年龄
    @Column(name = "age")
    private int age;
    // 所在医院
    @Column(name = "hospital")
    private String hospital;
    // 职位
    @Column(name = "position")
    private String position;
    //头像url
    @Column(name = "avatar_url")
    private String avatarUrl;
    //创建时间
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createDate;

    //修改时间
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDate;


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
