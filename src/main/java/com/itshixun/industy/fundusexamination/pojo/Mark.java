package com.itshixun.industy.fundusexamination.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Table(name="mark")
@Entity
@Data
public class Mark {
    //主键
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    private String id;
    //病例id,外键
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case caseEntity;
    //数据
    @Lob
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;
    //类型
    //原图12，视盘34，血管56，热力图7-16
    @Column(name = "image_type")
    private int imageType;



}
