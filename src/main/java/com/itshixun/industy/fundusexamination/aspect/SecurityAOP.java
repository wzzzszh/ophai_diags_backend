package com.itshixun.industy.fundusexamination.aspect;

import com.itshixun.industy.fundusexamination.annotation.UserPermission;
import com.itshixun.industy.fundusexamination.utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.domain.enums.UserPermissionEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@Aspect
public class SecurityAOP {


    @Pointcut("@annotation(com.itshixun.industy.fundusexamination.annotation.UserPermission)")
    public void userPermission() {
    }

    @Around("userPermission()")
    public Object userPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法上的注解
        UserPermission annotation = method.getAnnotation(UserPermission.class);
        UserPermissionEnum[] requiredPermissions = annotation.value();
        //从ThreadLocal中获取当前用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        UserPermissionEnum permission =  UserPermissionEnum.getByCode((Integer) claims.get("permission"));
        if (permission == UserPermissionEnum.ADMIN) {
            return joinPoint.proceed();
        }
        for (UserPermissionEnum requiredPermission : requiredPermissions) {
            if ( permission  ==  requiredPermission) {
                break;
            }else {
                throw new BusinessException(403,"权限不足");
            }
        }
        return joinPoint.proceed();

    }


}
