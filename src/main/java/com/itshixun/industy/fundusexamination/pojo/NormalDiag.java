package com.itshixun.industy.fundusexamination.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
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
@Data
@Entity
@Table(name = "normal_diag")
public class NormalDiag {
    // 主键
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    @Column(name = "diag_id")
    private String nDiagId;

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

}