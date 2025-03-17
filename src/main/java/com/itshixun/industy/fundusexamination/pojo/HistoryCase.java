
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
 * @Description 历史病例表
 * @Date 2023/4/18 15:40
 *
 **/
@Table(name="history_case")
@Entity
@Data
public class HistoryCase {
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    private String historyId;
    //患者信息
    @ManyToOne
    @JoinColumn(name = "patient_info_patient_id")
    private PatientInfo patientInfo;
    //标记一下
    @Column(name = "date")
    private Date date;
    @Column(name = "description")
    private String description;
    //创建时间
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    //更新时间
    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;


}