package com.connectu.connectuapi.exception;

public class EmailDuplicateException extends CreateUserException{
    public EmailDuplicateException() {
    }

    public EmailDuplicateException(Integer code, String message) {
        super(code, message);
    }

    public EmailDuplicateException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
