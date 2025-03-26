package com.itshixun.industy.fundusexamination.pojo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
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
@ToString(exclude = "caseEntity")
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
    // 医生姓名
    @Column(name = "doctor_name")
    private String doctorName;
    // 新增对Case的反向关联
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case caseEntity;
    @Column(name = "doc_suggestions")
    private String docSuggestions;

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

    @Override
    public String toString() {
        return "NormalDiag{" +
                "nDiagId='" + nDiagId + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", docSuggestions='" + docSuggestions + '\'' +
                '}';
    }
}