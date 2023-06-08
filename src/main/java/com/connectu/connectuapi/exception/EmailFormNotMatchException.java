package com.connectu.connectuapi.exception;

public class EmailFormNotMatchException extends CreateUserException{
    public EmailFormNotMatchException() {
    }

    public EmailFormNotMatchException(Integer code, String message) {
        super(code, message);
    }

    public EmailFormNotMatchException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
