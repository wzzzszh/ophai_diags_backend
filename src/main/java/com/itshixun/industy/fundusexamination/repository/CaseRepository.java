package com.itshixun.industy.fundusexamination.repository;

//import com.github.pagehelper.Page;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.NormalDiag;
import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<Case, String> {

    /**
     * 条件分页查询
     * @param diagStatus
     * @param diseaseNameJson
     * @param patientInfoPatientId
     * @param pageable
     * @return
     */
    @Query("SELECT c FROM Case c " +
            "WHERE (:diagStatus IS NULL OR c.diagStatus = :diagStatus) " +
            "AND (:diseaseNameJson IS NULL OR c.diseaseNameJson = :diseaseNameJson) " +
            "AND (:patientInfoPatientId IS NULL OR c.patientInfo.patientId = :patientInfoPatientId)"+
            "AND c.isDeleted = 0" +
            " ORDER BY c.createDate DESC")
    Page<Case> list(@Param("diagStatus") Integer diagStatus,
                    @Param("diseaseNameJson") String diseaseNameJson,
                    @Param("patientInfoPatientId") String patientInfoPatientId,
                    Pageable pageable
    );

    /**
     * 根据id逻辑删除
     * @param caseId
     */
    @Modifying
    @Transactional
    @Query("UPDATE Case c SET c.isDeleted = 1 WHERE c.caseId = :caseId")
    void updateById(@Param("caseId") String caseId);
    /**
     * 根据查询单个病例
     * @param caseId
     * @return
     */
    // 修改前（缺少事务注解）
    // @Transactional
    @Transactional
    @Query("SELECT c FROM Case c WHERE c.caseId = :caseId AND c.isDeleted = 0")
    Optional<Case> selectById(@Param("caseId") String caseId);



    /**
     * 根据患者信息查询
     * @param patientInfoPatientId
     * @return
     */
//    @Query("SELECT c FROM Case c " +
//            "WHERE (c.patientInfo.patientId = :patientInfoPatientId)"+
//            "AND c.isDeleted = 0")
    @Query
            ("select c from Case c where c.patientInfo.patientId = :patientInfoPatientId and c.isDeleted = 0")
    Page<Case>  findByPatientInfoPatientId(
            @Param("patientInfoPatientId")String patientInfoPatientId,
            Pageable pageable);
}
