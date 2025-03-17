package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.pojo.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByUserName(String userName);
    // 根据 userId 更新 avatarUrl 字段
    User findByUserId(String userId);

   
    boolean existsByIdNumber(@NotBlank(message = "身份号不能为空") @Size(min = 18, max = 18, message = "身份号必须为18位") @Pattern(regexp = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])[0-9]{2}\\d{3}[0-9X]$",
            message = "身份号格式不正确，需符合国家标准") String idNumber);

    boolean existsByUserName(@NotBlank(message = "用户名不能为空") String userName);
}
