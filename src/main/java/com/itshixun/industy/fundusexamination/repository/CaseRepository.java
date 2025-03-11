package com.itshixun.industy.fundusexamination.repository;

//import com.github.pagehelper.Page;
import com.itshixun.industy.fundusexamination.pojo.Case;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;

@Repository
public interface CaseRepository extends CrudRepository<Case, Integer>, JpaRepository<Case, Integer> {
    @Query("SELECT c FROM Case c " +
            "WHERE (:diagStatus IS NULL OR c.diagStatus = :diagStatus) " +
            "AND (:diseaseType IS NULL OR c.diseaseType = :diseaseType) " +
            "AND (:patientInfoPatientId IS NULL OR c.patientInfo.id = :patientInfoPatientId)"+
            "AND c.isDeleted = 0")
    Page<Case> list(@Param("diagStatus") Integer diagStatus,
                    @Param("diseaseType") Integer diseaseType,
                    @Param("patientInfoPatientId") Integer patientInfoPatientId,
                    Pageable pageable);
    @Modifying
    @Transactional
    @Query("UPDATE Case c SET c.isDeleted = 1 WHERE c.id = :id")
    void updateById(@Param("id") Integer id);

}
