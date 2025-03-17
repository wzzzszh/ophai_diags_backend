package com.itshixun.industy.fundusexamination.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @author 10169
 * @Description 智能诊断实体类
 * @Date 2023/4/18 15:40
 * **/
@Data
@Table(name="ai_diag")
@Entity
public class AiDiag {
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    private String aiDiagId;
    //疾病类型
    @Column(name="disease_type")
    private Integer diseaseType;
    //AI诊断结果列表1
    @Column(name="ai_diagnosis1")
    private String aiDiagnosis1;
    //AI诊断结果列表2
    @Column(name="ai_diagnosis2")
    private String aiDiagnosis2;
    //AI诊断结果列表3
    @Column(name="ai_diagnosis3")
    private String aiDiagnosis3;
    //Ai建议措施1
    @Column(name="ai_suggestion1")
    private String aiSuggestion1;
    //Ai建议措施2
    @Column(name="ai_suggestion2")
    private String aiSuggestion2;
    //Ai建议措施3
    @Column(name="ai_suggestion3")
    private String aiSuggestion3;
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
