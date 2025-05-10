package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.pojo.InvitationCode;
import org.springframework.data.repository.CrudRepository;

public interface InvitationCodeRepository extends CrudRepository<InvitationCode, String> {
    InvitationCode findByCode(String code);
}
