package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.OriginImageData;
import org.springframework.data.repository.CrudRepository;

public interface OriRepository extends CrudRepository<OriginImageData, String> {
}
