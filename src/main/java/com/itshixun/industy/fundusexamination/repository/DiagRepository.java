package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.pojo.NormalDiag;
import jdk.jshell.Diag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DiagRepository extends CrudRepository<NormalDiag, String> {
}
