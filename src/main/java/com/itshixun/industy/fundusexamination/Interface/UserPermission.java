package com.itshixun.industy.fundusexamination.Interface;

import com.itshixun.industy.fundusexamination.pojo.Enum.UserPermissionEnum;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface UserPermission {

    UserPermissionEnum[] value();
}
