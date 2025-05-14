package com.itshixun.industy.fundusexamination.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PatientRegisterDTO {

    @NotBlank(message = "姓名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5·•]{2,}$",
            message = "姓名格式应为某某某，例如：张三")
    @Length(min = 2, max = 20,message = "姓名长度不能少于2位")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$",
            message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",
            message = "身份证号格式错误")
    private String idCard;

    private String address;

    @NotBlank(message = "医保号不能为空")
    private String medicalCard;

    @NotBlank(message = "紧急联系人不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$",
            message = "紧急联系人手机号格式错误")
    private String emergencyContact;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 14,message = "密码长度不能少于6位")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[\\w!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]{6,16}$",
            message = "密码必须包含大小写字母和数字，不可包含空格")
    private String password;
}
