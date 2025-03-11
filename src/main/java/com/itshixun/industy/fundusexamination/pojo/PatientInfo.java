package com.itshixun.industy.fundusexamination.pojo;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 10169
 * @Description 患者基本信息表
 * @Date 2023/4/18 15:40
 *
**/
@Table(name =  "patient_info")
@Entity
public class PatientInfo {
    //主键外键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //患者id
    @Column(name = "patient_id")
    private Long id;

    //患者姓名
    @Column(name = "patient_name")
    private String name;
    //患者年龄
    @Column(name = "patient_age")
    private Integer age;
    //患者性别
    @Column(name = "patient_gender")
    private String gender;
    //创建时间
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    //更新时间
    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}