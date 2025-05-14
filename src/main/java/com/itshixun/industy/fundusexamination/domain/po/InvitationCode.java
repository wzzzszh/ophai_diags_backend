package com.itshixun.industy.fundusexamination.domain.po;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Table(name="invitation_code")
@Entity
@Data
public class InvitationCode {
    @Id
    @GeneratedValue(
            generator = "tableNameGenerator"
    )
    @GenericGenerator(
            name = "tableNameGenerator",
            strategy = "com.itshixun.industy.fundusexamination.utils.idGenetated.TableNameIdGenerator"
    )
    private String id;

    @Column(name = "code", unique = true)
    private String code;
}
