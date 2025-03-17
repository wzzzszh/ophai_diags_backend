// DiseaseRate.java
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
 * @Description 患病概率
 * @Date 2023/4/18 15:40
 *
 **/
@Data
@Table(name="disease_rate")
@Entity

//绑定case写77777
public class DiseaseRate {
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    private String diseaseRateId;
    @Column(name = "d_disease_rate")
    private Double dRate;    // 糖尿病概率
    @Column(name = "g_disease_rate")
    private Double gRate;     // 青光眼

    @Column(name = "c_disease_rate")
    private Double cRate;      // 白内障
    @Column(name = "a_disease_rate")
    private Double aRate;      // AMD（年龄相关性黄斑变性）
    @Column(name = "h_disease_rate")
    private Double hRate; // 高血压
    @Column(name = "m_disease_rate")
    private Double mRate;       // 近视
    @Column(name = "o_disease_rate")
    private Double otherRate; // 疾病概率
    //创建时间
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    //更新时间
    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    public String getDiseaseRateId() {
        return diseaseRateId;
    }

    public void setDiseaseRateId(String diseaseRateId) {
        this.diseaseRateId = diseaseRateId;
    }

    public Double getdRate() {
        return dRate;
    }

    public void setdRate(Double dRate) {
        this.dRate = dRate;
    }

    public Double getgRate() {
        return gRate;
    }

    public void setgRate(Double gRate) {
        this.gRate = gRate;
    }

    public Double getcRate() {
        return cRate;
    }

    public void setcRate(Double cRate) {
        this.cRate = cRate;
    }

    public Double getaRate() {
        return aRate;
    }

    public void setaRate(Double aRate) {
        this.aRate = aRate;
    }

    public Double gethRate() {
        return hRate;
    }

    public void sethRate(Double hRate) {
        this.hRate = hRate;
    }

    public Double getmRate() {
        return mRate;
    }

    public void setmRate(Double mRate) {
        this.mRate = mRate;
    }

    public Double getOtherRate() {
        return otherRate;
    }

    public void setOtherRate(Double otherRate) {
        this.otherRate = otherRate;
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