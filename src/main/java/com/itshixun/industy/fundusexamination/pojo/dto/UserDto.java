package com.itshixun.industy.fundusexamination.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户DTO
 */
@Data
public class UserDto {
    // 用户ID
    private String userId;
    // 用户名
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5·•]{2,}$",
            message = "用户名格式应为某某某，例如：张三")
    @Length(min = 2, max = 20,message = "用户名长度不能少于2位")
    private String userName;
    // SHA256加密密码
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 14,message = "密码长度不能少于6位")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[\\w!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]{6,16}$",
            message = "密码必须包含大小写字母和数字，不可包含空格")
    private String passwordHash;
    // 确认密码（仅用于注册/修改密码时校验，不存储）
    @NotBlank(message = "确认密码不能为空")
    @Size(min = 6, max = 14, message = "密码长度必须在6到14位之间")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[\\w!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]{6,16}$",
            message = "密码必须包含大小写字母和数字，不可包含空格")
    private String confirmPassword;
    // 身份证号
    @NotBlank(message = "身份号不能为空")
    @Size(min = 18, max = 18, message = "身份号必须为18位")
    @Pattern(regexp = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])[0-9]{2}\\d{3}[0-9X]$",
            message = "身份号格式不正确，需符合国家标准")
    private String idNumber;
    // 医生编号
    private Integer doctorNumber;
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
    @Pattern(regexp = "^[\\u4e00-\\u9fa5·•]{2,16}$",
            message = "请输入有效的中文姓名（2-16个汉字，可包含间隔号）")
    private String realName;
    // 性别
    // 0: 男
    // 1: 女
    private int gender;
    // 年龄
    private int age;
    // 所在医院
    private String hospital;
    // 职位
    private String position;
    // 头像URL
    private String avatarUrl;

}