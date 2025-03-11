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

        log.error("统一异常", e);
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
