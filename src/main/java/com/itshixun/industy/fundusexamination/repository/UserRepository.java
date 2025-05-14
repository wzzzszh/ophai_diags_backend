package com.itshixun.industy.fundusexamination.repository;

import com.itshixun.industy.fundusexamination.domain.po.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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

    // 分页查询所有非管理员用户
    @Query("SELECT u FROM User u " +
            "WHERE u.permission <> 4 " +
            "and u.permission <> 1 " +
            "ORDER BY u.permission DESC "
    )
    Page<User> findNonAdminUsers(Pageable pageable);

    @Query("SELECT u FROM User u " +
            "WHERE u.idNumber = :idCard"
    )
    User findByIdCard(String idCard);
}
