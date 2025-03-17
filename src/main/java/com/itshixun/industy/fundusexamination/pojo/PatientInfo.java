package com.itshixun.industy.fundusexamination.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 10169
 * @Description 患者基本信息表
 * @Date 2023/4/18 15:40
 *
**/
@Data
@Table(name =  "patient_info")
@Entity
public class PatientInfo {
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    @Column(name = "patient_id")
    private String patientId;

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



}