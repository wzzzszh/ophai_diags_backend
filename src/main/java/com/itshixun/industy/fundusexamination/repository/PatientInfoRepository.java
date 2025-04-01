package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.pojo.PageBean;
import com.itshixun.industy.fundusexamination.pojo.PatientInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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
}
