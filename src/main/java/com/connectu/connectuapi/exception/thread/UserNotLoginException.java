package com.connectu.connectuapi.exception.thread;

import com.connectu.connectuapi.controller.util.Code;

public class UserNotLoginException extends InsertThreadException{
    public UserNotLoginException() {
        super(Code.USER_NOT_LOGIN, "請登入後再新增文章");
    }
}
