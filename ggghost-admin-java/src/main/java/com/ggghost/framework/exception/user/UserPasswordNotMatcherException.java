package com.ggghost.framework.exception.user;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-17
 * @Description: 用户名货密码不匹配异常类
 * @Version: 1.0
 */

public class UserPasswordNotMatcherException extends UserException{
    public UserPasswordNotMatcherException() {
        super("username or password do not match","user.passwd.not.matcher");
    }
}
