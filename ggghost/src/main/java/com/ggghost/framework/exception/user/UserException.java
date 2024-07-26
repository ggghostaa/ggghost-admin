package com.ggghost.framework.exception.user;

import com.ggghost.framework.exception.BaseException;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-17
 * @Description: 用户异常类
 * @Version: 1.0
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;
    public UserException() {
        super("user", null, null);
    }
    public UserException(final String message) {
        super("user", null, message);
    }
    public UserException(final String code, final String message) {
        super("user", code, message);
    }
}
