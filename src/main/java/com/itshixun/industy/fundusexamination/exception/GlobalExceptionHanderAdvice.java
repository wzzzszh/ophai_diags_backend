package com.itshixun.industy.fundusexamination.exception;

import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHanderAdvice {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHanderAdvice.class);

    @ExceptionHandler({Exception.class})
    public ResponseMessage handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {

        // 获取堆栈信息
        StackTraceElement[] stackTrace = e.getStackTrace();
        String className = "未知类";
        String methodName = "未知方法";

        // 遍历堆栈寻找异常源头（跳过当前异常处理器）
        for (StackTraceElement element : stackTrace) {
            if (!element.getClassName().equals(this.getClass().getName())) {
                className = element.getClassName();
                methodName = element.getMethodName();
                break;
            }
        }

        // 记录带定位信息的日志
        log.error("【异常定位】类：{} 方法：{}", className, methodName, e);
        return new ResponseMessage(500,"error",null);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        Map<String, Object> errorResponse = new HashMap<>();
        if (fieldError!= null) {
            return new ResponseMessage(400,fieldError.getDefaultMessage(),null);
        } else {
            return new ResponseMessage(400,"参数验证出错",null);
        }
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseMessage handleBusinessException(BusinessException e) {
        log.error("{}:{}", e.getMessage(), e.getStackTrace()[0]);
        return new ResponseMessage(e.getErrorCode(),e.getMessage(),null);
    }
}
