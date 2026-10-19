package com.hospital.smartwait.common;

/**
 * 统一响应结果
 *
 * <p>对应需求规格说明书 5.1 接口设计约定：code=0 表示成功，非 0 为业务错误码，
 * message 为可直接展示给用户的中文提示，data 为业务数据。
 *
 * @param <T> 业务数据类型
 */
public class Result<T> {

    /** 成功码 */
    public static final int CODE_OK = 0;

    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 成功响应（带数据） */
    public static <T> Result<T> ok(T data) {
        return new Result<>(CODE_OK, "ok", data);
    }

    /** 成功响应（无数据） */
    public static Result<Void> ok() {
        return new Result<>(CODE_OK, "ok", null);
    }

    /** 失败响应 */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
