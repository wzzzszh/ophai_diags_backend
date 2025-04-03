// CaseEntity.java
package com.itshixun.industy.fundusexamination.pojo;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 10169
 * @Description 病例信息实体类
 * @Date 2023/4/18 15:40
 *
 **/
@Data
@Table(name = "cases")
@Entity
@ToString(exclude = "normalDiags")
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
    //一个json格式字段
    @Column(name = "ai_case_info", columnDefinition = "JSON") // MySQL专用语法
    private String aiCaseInfo;
    //医生诊断
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "nor_diag_id")
    private NormalDiag doctorDiag;
    // 修正为多个医生诊断
    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NormalDiag> doctorDiags = new ArrayList<>();;
    //责任医师（111）外键
    @Column(name = "responsible_doctor")
    private String responsibleDoctor;
    //诊断状态
    @Column(name = "diag_status")
    private Integer diagStatus;
    //疾病类型
    @Column(name = "disease_type")
    private Integer diseaseType;
    @Column(name = "disease_name")
    private String diseaseNameJson;
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
    // 自定义 json   * getter 和 * setter 方法
    public String[] getDiseaseName() {
        try {
            return new ObjectMapper().readValue(diseaseNameJson, String[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
    public void setDiseaseName(String[] diseaseName) {
        try {
            this.diseaseNameJson = new ObjectMapper().writeValueAsString(diseaseName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.diseaseNameJson = "[]";
        }
    }
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