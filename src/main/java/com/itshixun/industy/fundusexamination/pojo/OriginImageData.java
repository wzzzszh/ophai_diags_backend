package com.itshixun.industy.fundusexamination.pojo;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
//import javax.persistence.CascadeType;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 10169
 * @Description 原始影像表
 * @Date 2025/4/18 15:40
 *
 **/
@Table(name="ori_image")
@Entity
@Data
public class OriginImageData {


    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.Utils.IdGenetated.TableNameIdGenerator"
    )
    private String imageId;
    @Column(name = "left_image")
    private String leftImage;// 左眼URL或路径
    @Column(name = "right_image")
    private String rightImage;// 右眼URL或路径
    //创建时间
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    //更新时间
    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;
}