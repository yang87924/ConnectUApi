package com.connectu.connectuapi.controller.util;

import lombok.Data;


public class Code {

    //成功
    public static final Integer SAVE_OK = 20011;
    public static final Integer DELETE_OK = 20021;
    public static final Integer UPDATE_OK = 20031;
    public static final Integer GET_OK = 20041;
    public static final Integer LOGIN_OK = 20051;
    public static final Integer INVALIDATE_OK = 20061;
    public static final Integer CHAT_OK = 20071;




    //失敗
    public static final Integer SAVE_ERR = 20010;
    public static final Integer DELETE_ERR = 20020;
    public static final Integer UPDATE_ERR = 20030;
    public static final Integer GET_ERR = 20040;
    public static final Integer UNKOWN_ERR = 50000;
    public static final Integer SYSTEM_ERR = 50002;
    public static final Integer BUSINESS_ERR = 50003;


    //登入異常
    public static final Integer USER_IS_GOOGLE = 50011;
    public static final Integer USER_NOT_FOUND = 50012;
    public static final Integer PASSWORD_NOT_MATCH = 50013;



    //註冊帳號失敗
    public static final Integer COLUMN_IS_NULL = 50021;
    public static final Integer EMAIL_IS_DUPLICATE = 50022;
    public static final Integer EMAIL_NOT_FORMAT = 50023;
    public static final Integer PASSWORD_NOT_FORMAT = 50024;
    //文章新增失敗
    public static final Integer THREAD_COLUMN_IS_NULL = 50025;
    public static final Integer USER_NOT_LOGIN = 50026;
    public static final Integer FAVORITE_THREAD_NOT_FOUND = 50027;


    //檔案上傳失敗
    public static final Integer FILE_IS_EMPTY = 50031;
    public static final Integer FILE_SIZE_ERR = 50032;
    public static final Integer FILE_STATE_ERR = 50033;
    public static final Integer FILE_TYPE_ERR = 50034;
    public static final Integer FILE_UPLOAD_IO_ERR = 50035;






}
