package com.ggghost.framework.exception.user;

import com.ggghost.framework.exception.BaseException;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-17
 * @Description: 用户未不存在
 * @Version: 1.0
 */
public class UserNotFoundException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("user not found","user.not.found");
    }
}
