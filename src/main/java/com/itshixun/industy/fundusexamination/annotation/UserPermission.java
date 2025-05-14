package com.itshixun.industy.fundusexamination.annotation;

import com.itshixun.industy.fundusexamination.domain.enums.UserPermissionEnum;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface UserPermission {

    UserPermissionEnum[] value();
}
