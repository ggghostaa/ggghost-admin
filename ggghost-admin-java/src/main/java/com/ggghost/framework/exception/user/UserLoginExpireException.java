package com.ggghost.framework.exception.user;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-25
 * @Description: 用户登录已过期
 * @Version: 1.0
 */
public class UserLoginExpireException extends UserException{
    private static final long serialVersionUID = 1L;
    public UserLoginExpireException() {
        super("user login expire","user.login.expire");
    }
}
