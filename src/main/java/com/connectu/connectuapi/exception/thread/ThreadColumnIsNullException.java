package com.connectu.connectuapi.exception.thread;

import com.connectu.connectuapi.controller.util.Code;

public class ThreadColumnIsNullException extends InsertThreadException {
    public ThreadColumnIsNullException() {
        super(Code.THREAD_COLUMN_IS_NULL, "欄位不能為空");
    }
}