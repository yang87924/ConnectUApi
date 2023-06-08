package com.connectu.connectuapi.exception;

public class PasswordFormNotMatchException extends CreateUserException{
    public PasswordFormNotMatchException() {
    }

    public PasswordFormNotMatchException(Integer code, String message) {
        super(code, message);
    }

    public PasswordFormNotMatchException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
