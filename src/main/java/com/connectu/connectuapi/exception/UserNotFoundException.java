package com.connectu.connectuapi.exception;

public class UserNotFoundException extends ServiceException{
    public UserNotFoundException() {
    }

    public UserNotFoundException(Integer code, String message) {
        super(code, message);
    }

    public UserNotFoundException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
