package com.connectu.connectuapi.exception;

import com.connectu.connectuapi.controller.util.Code;
import lombok.Data;


public class ColumnIsNullException extends CreateUserException{
    public ColumnIsNullException() {
        super(Code.COLUMN_IS_NULL, "欄位不能為空");
    }
}
