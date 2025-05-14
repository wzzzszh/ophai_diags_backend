package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.domain.po.Case;
import org.springframework.data.repository.CrudRepository;

public interface PreImageRepository extends CrudRepository<Case, String> {
}
