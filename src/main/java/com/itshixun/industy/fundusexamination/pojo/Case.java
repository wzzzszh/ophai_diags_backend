// CaseEntity.java
package com.itshixun.industy.fundusexamination.pojo;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 * @author 10169
 * @Description 病例信息实体类
 * @Date 2023/4/18 15:40
 *
 **/
@Data
@Table(name = "cases")
@Entity
public class Case {
    //病例id
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    private String caseId;
    //患者信息
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_info_patient_id")
    private PatientInfo patientInfo;
    //原始照片
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "oriimage_data_id")
    private OriginImageData originImageData;
    //预处理照片
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preimage_data_id")
    private PreImageData preImageData;
    //疾病概率表
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "rate_id")
    private DiseaseRate diseaseRate;
    //ai诊断
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ai_diag_id")
    private AiDiag aiDiag;
    //医生诊断
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "nor_diag_id")
    private NormalDiag normalDiag;
    //责任医师（111）外键
    @Column(name = "responsible_doctor")
    private String responsibleDoctor;
    //诊断状态
    @Column(name = "diag_status")
    private Integer diagStatus;
    //疾病类型
    @Column(name = "disease_type")
    private Integer diseaseType;
    //最后更新时间
    @Column(name = "last_update_date")
    private Date lastUpdateDate;
    //创建时间
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    //更新时间
    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;
    // 逻辑删除标志（0表示有效，1表示已删除）
    @Column(name = "is_deleted", columnDefinition = "int default 0")
    private Integer isDeleted;

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

}