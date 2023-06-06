package com.connectu.connectuapi.exception;

import com.connectu.connectuapi.controller.Code;
import com.connectu.connectuapi.controller.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


public class SystemException extends RuntimeException{
    private Integer code;
    private String message;


    public SystemException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public SystemException(Integer code, String message, Throwable cause) {
        super(cause);
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
