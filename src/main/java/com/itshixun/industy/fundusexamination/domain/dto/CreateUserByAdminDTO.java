package com.itshixun.industy.fundusexamination.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateUserByAdminDTO {
    // 用户名
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5·•]{2,}$",
            message = "用户名格式应为某某某，例如：张三")
    @Length(min = 2, max = 20,message = "用户名长度不能少于2位")
    private String userName;
    // SHA256加密密码
    private String passwordHash = "123456";
    // 身份证号
    @NotBlank(message = "身份号不能为空")
    @Size(min = 18, max = 18, message = "身份号必须为18位")
    @Pattern(regexp = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])[0-9]{2}\\d{3}[0-9X]$",
            message = "身份号格式不正确，需符合国家标准")
    private String idNumber;

    // 邮箱
    @Email(message = "邮箱格式不正确")
    private String email;
    // 手机号
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号必须为11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$",
            message = "手机号格式不正确，需以1开头且第二位为3-9")
    private String phone;
    // 真实姓名
//    @Pattern(regexp = "^[\\u4e00-\\u9fa5·•]{2,16}$",
//            message = "请输入有效的中文姓名（2-16个汉字，可包含间隔号）")
//    private String realName;
    // 性别
    // 0: 男
    // 1: 女
    private int gender;
    // 年龄
    private int age;
    // 职位
    private String position;
    // 头像URL
    private String avatarUrl;
    // 权限
    @NotNull(message = "权限不能为空")
    @Min(value = 2, message = "权限必须大于等于2")
    @Max(value = 3, message = "权限必须小于等于3")

    private int permission;

}
