package com.itshixun.industy.fundusexamination.Aspect;

import com.itshixun.industy.fundusexamination.Interface.UserPermission;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;
import com.itshixun.industy.fundusexamination.exception.BusinessException;
import com.itshixun.industy.fundusexamination.pojo.Enum.UserPermissionEnum;
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


    @Pointcut("@annotation(com.itshixun.industy.fundusexamination.Interface.UserPermission)")
    public void userPermission() {
    }

    @Around("userPermission()")
    public Object userPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法上的注解
        UserPermission annotation = method.getAnnotation(UserPermission.class);
        UserPermissionEnum[] requiredPermissions = annotation.value();

//        int requiredPermissionCode = requiredPermission.getCode();
//        System.out.println("恭喜你获取到了注解value："+requiredPermission);
        //从ThreadLocal中获取用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        UserPermissionEnum permission =  UserPermissionEnum.getByCode((Integer) claims.get("permission"));

//        UserDto userDto = new UserDto();
//        userDto.setUserId(userId);
//        User user = userService.findByUserId(userDto);
//        System.out.println("恭喜你获取到了用户信息："+user);
        for (UserPermissionEnum requiredPermission : requiredPermissions) {
            if ( permission  == requiredPermission) {
                break;
            }else {
                throw new BusinessException(403,"权限不足");
            }
        }

        return joinPoint.proceed();

    }


}
