package com.connectu.connectuapi.exception.file;

/** 上传的文件为空的异常，例如没有选择上传的文件就提交了表单，或选择的文件是0字节的空文件 */
public class FileEmptyException extends FileUploadException {
    public FileEmptyException() {
    }

    public FileEmptyException(Integer code, String message) {
        super(code, message);
    }

    public FileEmptyException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
