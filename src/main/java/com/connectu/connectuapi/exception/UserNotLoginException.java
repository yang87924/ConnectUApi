package com.connectu.connectuapi.exception;

import com.connectu.connectuapi.controller.util.Code;

public class UserNotLoginException extends ServiceException{
    public UserNotLoginException() {
    }

    public UserNotLoginException(Integer code, String message) {
        super(code, message);
    }

    public UserNotLoginException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
