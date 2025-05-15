package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.domain.po.Case;
import com.itshixun.industy.fundusexamination.domain.po.Mark;
import com.itshixun.industy.fundusexamination.utils.ThreadLocalUtil;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarkRepository extends CrudRepository<Mark, String> {
    List<Mark> findAllByCaseEntity_caseId(String caseId);

    ThreadLocalUtil findByCaseEntity(Case caseEntity);
    /**
     * 根据caseId逻辑删除
     * @param caseId
     */
//    @Modifying
//    @Query("UPDATE Mark m SET m.Deleted = 1 WHERE m.caseEntity.caseId = :caseId")
//    void updateById(String caseId);
}
