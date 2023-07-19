package com.connectu.connectuapi.exception;

import com.connectu.connectuapi.controller.util.Code;

public class ThreadColumnIsNullException extends ServiceException {
    public ThreadColumnIsNullException() {
    }

    public ThreadColumnIsNullException(Integer code, String message) {
        super(code, message);
    }

    public ThreadColumnIsNullException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}