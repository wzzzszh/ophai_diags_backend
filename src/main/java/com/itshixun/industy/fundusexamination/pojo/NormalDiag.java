package com.itshixun.industy.fundusexamination.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
/**
 * @author 10169
 * @Description 医生诊断
 * @Date 2025/4/18 15:40
 *
 **/
@Entity
@Table(name = "normal_diag")
public class NormalDiag {
    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 更新日期
//    private Date date;

    // 医生姓名
    @Column(name = "doctor_name")
    private String doctorName;

    // 所属机构
    @Column(name = "department")
    private String department;

    // 医生建议（使用方案一映射）
    @ElementCollection // 声明基础类型集合
    @CollectionTable(
            name = "normal_diag_suggestions", // 关联表名称
            joinColumns = @JoinColumn(name = "diag_id") // 主表关联字段
    )
    @Column(name = "suggestion") // 单条建议的列名
    private List<String> DocSuggestions;

    //创建时间
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createDate;

    //修改时间
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getDocSuggestions() {
        return DocSuggestions;
    }

    public void setDocSuggestions(List<String> docSuggestions) {
        DocSuggestions = docSuggestions;
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