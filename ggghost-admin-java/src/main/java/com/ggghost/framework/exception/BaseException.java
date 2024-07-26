package com.ggghost.framework.exception;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-16
 * @Description:基础异常类
 * @Version: 1.0
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String module;//模块
    private String code;//错误码
    private String msg;//错误信息

    public BaseException() {
    }

    public BaseException(String msg) {
        this.msg = msg;
    }

    public BaseException(String module, String code, String msg) {
        this.module = module;
        this.code = code;
        this.msg = msg;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
