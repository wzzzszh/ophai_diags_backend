package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.pojo.NormalDiag;
import com.itshixun.industy.fundusexamination.pojo.dto.NormalDiagDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// NormalDiagRepository.java
public interface NormalDiagRepository extends JpaRepository<NormalDiag, String> {
    // 根据 caseId 查询所有关联的诊断
    // 新增查询方法
//    @Query("SELECT nd FROM NormalDiag nd WHERE nd.caseEntity.caseId = :caseId")
//    List<NormalDiag> findNormalDiagsByCaseId(@Param("caseId") String caseId);
    @Query("SELECT nd.createDate, nd.nDiagId,nd.docSuggestions,nd.doctorName,nd.updateDate FROM NormalDiag nd WHERE nd.caseEntity.caseId = :caseId")
    List<Object[]> findNormalDiagsByCaseId(@Param("caseId") String caseId);
//    List<NormalDiagDto> findNormalDiagsByCaseId1(@Param("caseId") String caseId);
    // ... existing code ...
    // 修改后的查询方法，使用构造函数表达式将结果映射到 DTO 类
//    @Query("SELECT new com.itshixun.industy.fundusexamination.pojo.dto.NormalDiagDto(nd.nDiagId, nd.doctorName, nd.docSuggestions) FROM NormalDiag nd WHERE nd.caseEntity.caseId = :caseId")
//    List<NormalDiagDto> findNormalDiagsByCaseId3(@Param("caseId") String caseId);
//    // ... existing code ...
}
