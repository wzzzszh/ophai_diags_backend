package com.itshixun.industy.fundusexamination.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotBlank(message = "密码不能为空")
    private String passwordHash;
}
