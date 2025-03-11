package com.itshixun.industy.fundusexamination.Utils;

import com.itshixun.industy.fundusexamination.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ResponseMessage<T> {
    private Integer code;
    private String message;
    private T data;
    public ResponseMessage(Integer code,String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    //登录认证失败
    public static <T> ResponseMessage<T> loginerror() {
//        return new ResponseMessage(HttpStatus.OK.value(),"登录认证失败",null);
        throw new BusinessException(401,"登录认证失败");
    }

    //
    public static <T> ResponseMessage<T> success(String token) {
        return new ResponseMessage(HttpStatus.OK.value(),"success",token);
    }
    //接口请求失败
    public static <T> ResponseMessage<T> success() {

        return new ResponseMessage(HttpStatus.OK.value(),"success",null);
    }
    //接口请求成功
    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage(HttpStatus.OK.value(),"success",data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
