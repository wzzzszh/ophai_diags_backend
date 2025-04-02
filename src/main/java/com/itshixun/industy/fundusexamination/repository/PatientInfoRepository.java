package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatientInfoRepository extends CrudRepository<PatientInfo, String> {
    @Query("SELECT p FROM PatientInfo p " +
            "WHERE (:age IS NULL OR p.age = :age) " +
            "AND (:name IS NULL OR p.name = :name) " +
            "AND (:patientId IS NULL OR p.patientId = :patientId) " +
            "AND (:gender IS NULL OR p.gender = :gender)" +
            "AND p.isDelete = 0")
    Page<PatientInfo> findPatientsByCriteria(Pageable pageable,
                                             @Param("age") Integer age,
                                             @Param("name") String name,
                                             @Param("patientId") String patientId,
                                             @Param("gender")Integer gender) ;


    /**
     * 根据patientId查询患者信息
     * @param patientId
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM PatientInfo p " +
            "WHERE (:patientId IS NULL OR p.patientId = :patientId)" +
            "AND p.isDelete = 0")
    Page<PatientInfo> findPatientsByPatientId(
            @Param("patientId") String patientId,
            Pageable pageable);

    /**
     * 根据name查询患者信息
     * @param name
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM PatientInfo p" +
            " WHERE (:name IS NULL OR p.name = :name)" +
            " AND p.isDelete = 0" +
            " ORDER BY p.createDate DESC")
    Page<PatientInfo> findPatientsByName(
            @Param("name") String name,
            Pageable pageable);

    /**
     * 逻辑删除
     *
     * @param patientId
     */
    @Query("UPDATE PatientInfo p SET p.isDelete = 1 WHERE p.patientId = :patientId")
    @Modifying
    @Transactional
    int deleteByIdO(String patientId);
    @Query("select p from PatientInfo p where (p.patientId = :patientId) and (p.isDelete = 0)")
    Optional<PatientInfo> selectById(@Param("patientId") String patientId);
}
