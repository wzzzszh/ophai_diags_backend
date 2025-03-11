// DoctorSuggestionDto.java
package com.itshixun.industy.fundusexamination.pojo;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 * @author 10169
 * @Description 医生建议模块
 * @Date 2023/4/18 15:40
 *
 **/
@Table(name = "doc_suggestion")
@Entity
public class DoctorSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "case_id")
    private String name;
    private String position;
    private String suggestions;
    //创建时间
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    //更新时间
    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;
}