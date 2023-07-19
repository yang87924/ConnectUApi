package com.connectu.connectuapi.exception;

public class PasswordNotMatchException extends ServiceException {
    public PasswordNotMatchException() {
    }

    public PasswordNotMatchException(Integer code, String message) {
        super(code, message);
    }

    public PasswordNotMatchException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
