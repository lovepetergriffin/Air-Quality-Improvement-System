package com.cqupt.nep.utils;

public class ResultVo<T> {
    private boolean flag;
    private Integer code;
    private String message;
    private T data;

    public static <T> ResultVo<T> ok(T data) {
        ResultVo<T> result = new ResultVo<T>();
        result.setFlag(true);
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> ResultVo<T> ok(T data, String message) {
        ResultVo<T> result = ok(data);
        result.setMessage(message);
        return result;
    }

    public static <T> ResultVo<T> fail(String message) {
        ResultVo<T> result = new ResultVo<T>();
        result.setFlag(false);
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getMsg() {
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
