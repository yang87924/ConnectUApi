package com.connectu.connectuapi.exception.file;

/** 上传的文件状态异常 */
public class FileStateException extends FileUploadException {
    public FileStateException() {
    }

    public FileStateException(Integer code, String message) {
        super(code, message);
    }

    public FileStateException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
