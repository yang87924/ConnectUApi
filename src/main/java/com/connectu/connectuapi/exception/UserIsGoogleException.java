package com.connectu.connectuapi.exception;

public class UserIsGoogleException extends ServiceException{
    public UserIsGoogleException() {
    }

    public UserIsGoogleException(Integer code, String message) {
        super(code, message);
    }

    public UserIsGoogleException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
