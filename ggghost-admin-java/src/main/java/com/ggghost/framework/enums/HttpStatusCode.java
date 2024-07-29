package com.ggghost.framework.enums;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-15
 * @Description: http状态码
 * @Version: 1.0
 */
public enum HttpStatusCode {
    // 信息性状态码
    CONTINUE(100, "继续"),
    // 成功状态码
    OK(200, "请求成功"),
    CREATED(201, "已创建"),
    ACCEPTED(202, "已接受"),
    NO_CONTENT(204, "无内容"),
    // 重定向状态码
    MOVED_PERMANENTLY(301, "永久移动"),
    FOUND(302, "找到"),
    NOT_MODIFIED(304, "未修改"),
    // 客户端错误状态码
    BAD_REQUEST(400, "错误请求"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "未找到"),
    // 服务器错误状态码
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    NOT_IMPLEMENTED(501, "未实现"),
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private final int code;
    private final String description;

    HttpStatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
