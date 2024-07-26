package com.ggghost.framework.enums;

import com.ggghost.framework.exception.BaseErrorInfoInterface;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-16
 * @Description: 错误码
 * @Version: 1.0
 */
public enum ExceptionEnum implements BaseErrorInfoInterface {
    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NO_USER_ERROR(40102, "用户不存在"),
    USERNAME_OR_PASSWORD_NOT_MATCHER(40103, "账号或密码不正确"),
    USER_LOGIN_EXPIRE(40104, "用户登录已过期,请重新登录"),
    USER_AUTHENTICATION_EXPIRE(40105, "用户认证异常,请重新登录"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    TOO_MANY_REQUEST(42300, "请求频繁"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    private int errorCode;
    private String errorMsg;

    ExceptionEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
