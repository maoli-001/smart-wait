package com.hospital.smartwait.common;

/**
 * 业务异常：携带业务错误码与面向用户的中文提示，
 * 由 {@link GlobalExceptionHandler} 统一转换为 {@link Result} 响应。
 */
public class BizException extends RuntimeException {

    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
