// CaseEntity.java
package com.itshixun.industy.fundusexamination.pojo;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 * @author 10169
 * @Description 病例信息实体类
 * @Date 2023/4/18 15:40
 *
 **/
@Table(name = "cases")
@Entity
public class Case {
    //病例id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_id")
    private Integer id;
    //患者信息
    @ManyToOne
    @JoinColumn(name = "patient_info_patient_id")
    private PatientInfo patientInfo;
    //原始照片
    @OneToOne
    @JoinColumn(name = "oriimage_data_id")
    private OriginImageData originImageData;
    //预处理照片
    @OneToOne
    @JoinColumn(name = "preimage_data_id")
    private PreImageData preImageData;
    //疾病概率表
    @OneToOne
    @JoinColumn(name = "rate_id")
    private DiseaseRate diseaseRate;
    //ai诊断
    @OneToOne
    @JoinColumn(name = "ai_diag_id")
    private AiDiag aiDiag;
    //医生诊断
    @OneToOne
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

    public OriginImageData getOriginImageData() {
        return originImageData;
    }

    public void setOriginImageData(OriginImageData originImageData) {
        this.originImageData = originImageData;
    }

    public PreImageData getPreImageData() {
        return preImageData;
    }

    public void setPreImageData(PreImageData preImageData) {
        this.preImageData = preImageData;
    }

    public DiseaseRate getDiseaseRate() {
        return diseaseRate;
    }

    public void setDiseaseRate(DiseaseRate diseaseRate) {
        this.diseaseRate = diseaseRate;
    }

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResponsibleDoctor() {
        return responsibleDoctor;
    }

    public void setResponsibleDoctor(String responsibleDoctor) {
        this.responsibleDoctor = responsibleDoctor;
    }

    public Integer getDiagStatus() {
        return diagStatus;
    }

    public void setDiagStatus(Integer diagStatus) {
        this.diagStatus = diagStatus;
    }

    public Integer getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(Integer diseaseType) {
        this.diseaseType = diseaseType;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public AiDiag getAiDiag() {
        return aiDiag;
    }

    public void setAiDiag(AiDiag aiDiag) {
        this.aiDiag = aiDiag;
    }

    public NormalDiag getNormalDiag() {
        return normalDiag;
    }

    public void setNormalDiag(NormalDiag normalDiag) {
        this.normalDiag = normalDiag;
    }

    @Override
    public String toString() {
        return "Case{" +
                "id=" + id +
                ", patientInfo=" + patientInfo +
                ", originImageData=" + originImageData +
                ", preImageData=" + preImageData +
                ", diseaseRate=" + diseaseRate +
                ", aiDiag=" + aiDiag +
                ", normalDiag=" + normalDiag +
                ", responsibleDoctor='" + responsibleDoctor + '\'' +
                ", diagStatus=" + diagStatus +
                ", diseaseType=" + diseaseType +
                ", lastUpdateDate=" + lastUpdateDate +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", isDeleted=" + isDeleted +
                '}';
    }
}