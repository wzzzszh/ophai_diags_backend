package com.itshixun.industy.fundusexamination.domain.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author 10169
 * @Description 患者基本信息表
 * @Date 2023/4/18 15:40
 *
**/
@Data
@Table(name =  "patient_info")
@Entity
@ToString(exclude = "cases")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PatientInfo {
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.utils.idGenetated.TableNameIdGenerator"
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
    private Integer gender;

    //患者电话
    @Column(name = "patient_phone")
    private String phone;

    //患者身份证号
    @Column(name = "patient_idcard")
    private String idCard;

    //患者地址
    @Column(name = "patient_address")
    private String address;

    // 患者医保号
    @Column(name = "patient_medical_card")
    private String medicalCard;

    // 紧急联系人电话
    @Column(name = "patient_emergency_contact")
    private String emergencyContact;

    //创建时间
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    //更新时间
    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;
    //逻辑删除字段,默认值为0
    @Column(name = "is_delete", columnDefinition = "int default 0")
    private Integer isDelete;

}