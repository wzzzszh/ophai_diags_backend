package com.itshixun.industy.fundusexamination.exception;

public class BusinessException extends RuntimeException{
    private int errorCode;
    private String message;
    public BusinessException(int errorCode, String message){
        this.message = message;
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
