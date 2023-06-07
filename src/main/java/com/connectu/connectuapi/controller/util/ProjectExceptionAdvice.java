package com.connectu.connectuapi.controller.util;


import com.connectu.connectuapi.exception.PasswordNotMatchException;
import com.connectu.connectuapi.exception.ServiceException;
import com.connectu.connectuapi.exception.SystemException;
import com.connectu.connectuapi.exception.UserNotFoundException;
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

    @ExceptionHandler(ServiceException.class)
    public Result doBusinessException(ServiceException ex){
        Result result = new Result(Code.BUSINESS_ERR,null,"業務系統忙碌中");
        if(ex instanceof UserNotFoundException){
            result.setCode(Code.USER_NOT_FOUND);
            result.setMsg("查無此用戶");
        } else if (ex instanceof PasswordNotMatchException) {
            result.setCode(Code.PASSWORD_NOT_MATCH);
            result.setMsg("密碼錯誤");
        }
        return result;
    }

}
