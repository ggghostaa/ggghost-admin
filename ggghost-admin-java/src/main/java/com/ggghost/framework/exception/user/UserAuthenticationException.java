package com.ggghost.framework.exception.user;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-25
 * @Description: 用户认证异常
 * @Version: 1.0
 */
public class UserAuthenticationException extends UserException{
    private static final long serialVersionUID = 1L;
    public UserAuthenticationException() {
        super("user authentication","user.authentication");
    }
}
