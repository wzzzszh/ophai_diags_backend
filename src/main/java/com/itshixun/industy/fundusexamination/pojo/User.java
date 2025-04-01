package com.itshixun.industy.fundusexamination.pojo;




import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author 10169
 * @Description 用户表
 * @Date 2025/4/18 15:40
 *
 */
@lombok.Data
@Table(name="tb_user")
@Entity

public class User {
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    private String userId;

    // 用户名
    @Column(name = "user_name")
    private String userName;
    // Md5加密
    @Column(name = "password_hash")
    private String passwordHash;
    //身份证号
    @Column(name = "id_number")
    private String idNumber;
    //医师证号码
    @Column(name = "doctor_number")
    private Integer doctorNumber;
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
    private LocalDateTime createDate;

    //修改时间
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDate;
}
