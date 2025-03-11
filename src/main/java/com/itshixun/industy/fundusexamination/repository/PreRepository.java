package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.pojo.OriginImageData;
import com.itshixun.industy.fundusexamination.pojo.PreImageData;
import org.springframework.data.repository.CrudRepository;

public interface PreRepository extends CrudRepository<PreImageData, Integer> {
}
