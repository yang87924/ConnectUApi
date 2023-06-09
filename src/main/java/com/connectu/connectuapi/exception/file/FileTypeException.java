package com.connectu.connectuapi.exception.file;

public class FileTypeException extends FileUploadException {
    public FileTypeException() {
    }

    public FileTypeException(Integer code, String message) {
        super(code, message);
    }

    public FileTypeException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
