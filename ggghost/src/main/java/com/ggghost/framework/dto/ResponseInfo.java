package com.ggghost.framework.dto;

import com.ggghost.framework.enums.ExceptionEnum;
import com.ggghost.framework.enums.HttpStatusCode;
import com.ggghost.framework.exception.BaseException;

import java.io.Serializable;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-15
 * @Description: 响应结果封装
 * @Version: 1.0
 */
public class ResponseInfo <T> implements Serializable {
    private int code;//响应码
    private T data;//响应数据
    private String message;//响应信息
    private String status;//状态

    /**
     * 请求失败
     * @param httpStatusCode
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResponseInfo<T> fail(HttpStatusCode httpStatusCode, String message) {
        return new ResponseInfo<>(httpStatusCode.getCode(), message);
    }

    public static <T> ResponseInfo<T> fail(ExceptionEnum exceptionEnum) {
        return new ResponseInfo<>(exceptionEnum.getErrorCode(), exceptionEnum.getErrorMsg());
    }

    /**
     * 请求失败
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResponseInfo<T> fail(String message) {
        return new ResponseInfo<>(HttpStatusCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 请求成功
     * @param data
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResponseInfo<T> success(T data, String message) {
        return new ResponseInfo<T>(data, message);
    }

    /**
     * 请求成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseInfo<T> success(T data) {
        return new ResponseInfo<T>(data);
    }

    /**
     * 请求成功
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResponseInfo<T> success(String message) {
        return new ResponseInfo<T>(message);
    }

    public ResponseInfo() {
        this.code = HttpStatusCode.OK.getCode();
        this.status = HttpStatusCode.OK.getDescription();
    }
    public ResponseInfo(HttpStatusCode statusEnums) {
        this.code = statusEnums.getCode();
        this.message = statusEnums.getDescription();
    }
    public ResponseInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public ResponseInfo(T data, String message) {
        this.code = HttpStatusCode.OK.getCode();
        this.status = HttpStatusCode.OK.getDescription();
        this.data = data;
        this.message = message;
    }
    public ResponseInfo(String message) {
        this.code = HttpStatusCode.OK.getCode();
        this.status = HttpStatusCode.OK.getDescription();
        this.message = message;
    }
    public ResponseInfo(T data) {
        this.code = HttpStatusCode.OK.getCode();
        this.status = HttpStatusCode.OK.getDescription();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
