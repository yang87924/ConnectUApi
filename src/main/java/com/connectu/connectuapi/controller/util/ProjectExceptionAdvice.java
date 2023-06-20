package com.connectu.connectuapi.controller.util;


import com.connectu.connectuapi.exception.*;
import com.connectu.connectuapi.exception.file.*;
import com.connectu.connectuapi.exception.ThreadColumnIsNullException;
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
        } else if (ex instanceof UserIsGoogleException) {
            result.setCode(Code.USER_IS_GOOGLE);
            result.setMsg("請使用GOOGLE登入");
        } else if (ex instanceof PasswordNotMatchException) {
            result.setCode(Code.PASSWORD_NOT_MATCH);
            result.setMsg("密碼錯誤");
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
        } else if (ex instanceof FileEmptyException) {
            result.setCode(Code.FILE_IS_EMPTY);
            result.setMsg("上傳的文件不允許為空");
        } else if (ex instanceof FileSizeException) {
            result.setCode(Code.FILE_SIZE_ERR);
            result.setMsg("不允許上傳超過10MB的文件");
        } else if (ex instanceof FileStateException) {
            result.setCode(Code.FILE_STATE_ERR);
            result.setMsg("文件狀態異常，可能文件已被移動或刪除");
        } else if (ex instanceof FileTypeException) {
            result.setCode(Code.FILE_TYPE_ERR);
            result.setMsg("不支援使用該類型的文件");
        } else if (ex instanceof FileUploadIOException) {
            result.setCode(Code.FILE_UPLOAD_IO_ERR);
            result.setMsg("上傳文件時讀寫錯誤，請稍後重新嘗試");
        } else if (ex instanceof ThreadColumnIsNullException) {
            result.setCode(Code.THREAD_COLUMN_IS_NULL);
            result.setMsg("文章資訊未輸入");
        } else if (ex instanceof UserNotLoginException) {
            result.setCode(Code.USER_NOT_LOGIN);
            result.setMsg("請登入後再進行操作");
        }
        return result;
    }



}
