package com.connectu.connectuapi.exception.file;

/** 上传的文件的大小超出了限制值 */
public class FileSizeException extends FileUploadException {
    public FileSizeException() {
    }

    public FileSizeException(Integer code, String message) {
        super(code, message);
    }

    public FileSizeException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
