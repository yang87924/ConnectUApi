package com.connectu.connectuapi.exception.file;


public class FileUploadIOException extends FileUploadException {
    public FileUploadIOException() {
    }

    public FileUploadIOException(Integer code, String message) {
        super(code, message);
    }

    public FileUploadIOException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
