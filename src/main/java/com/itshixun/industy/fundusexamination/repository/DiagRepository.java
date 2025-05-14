package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.domain.po.NormalDiag;
import org.springframework.data.repository.CrudRepository;

public interface DiagRepository extends CrudRepository<NormalDiag, String> {
}
