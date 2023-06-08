package com.connectu.connectuapi.controller.util;


import com.connectu.connectuapi.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public Result doException(Exception ex){

        return new Result(Code.UNKOWN_ERR,null,"伺服器忙碌中");
    }

    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex){

        return new Result(Code.SYSTEM_ERR, null, "系統異常，伺服器忙碌中");
    }

    @ExceptionHandler({ServiceException.class, FileUploadException.class, CreateUserException.class})
    public Result doBusinessException(Throwable ex){
        Result result = new Result(Code.BUSINESS_ERR,null,"業務系統忙碌中");
        if(ex instanceof UserNotFoundException){
            result.setCode(Code.USER_NOT_FOUND);
            result.setMsg("查無此用戶");
        } else if (ex instanceof PasswordNotMatchException) {
            result.setCode(Code.PASSWORD_NOT_MATCH);
            result.setMsg("密碼錯誤");
        } else if (ex instanceof FileUploadException) {
            result.setCode(Code.FILE_UPLOAD_ERROR);
            result.setMsg("文件上傳失敗");
        } else if (ex instanceof ColumnIsNullException) {
            result.setCode(Code.COLUMN_IS_NULL);
            result.setMsg("欄位不可為空");
        } else if (ex instanceof EmailFormNotMatchException) {
            result.setCode(Code.EMAIL_NOT_FORMAT);
            result.setMsg("信箱格式錯誤");
        } else if (ex instanceof EmailDuplicateException) {
            result.setCode(Code.EMAIL_IS_DUPLICATE);
            result.setMsg("信箱重複");
        } else if (ex instanceof PasswordFormNotMatchException) {
            result.setCode(Code.PASSWORD_NOT_FORMAT);
            result.setMsg("密碼格式錯誤");
        }
        return result;
    }



}
