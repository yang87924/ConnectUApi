package com.connectu.connectuapi.controller;


import com.connectu.connectuapi.exception.BusinessException;
import com.connectu.connectuapi.exception.SystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public Result doException(Exception ex){
        return new Result(Code.UNKOWN_ERR,null,"Exception!");
    }

    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex){
        return new Result(ex.getCode(), null, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException ex){
        return new Result(ex.getCode(),null,ex.getMessage());
    }

}
