package com.ggghost.framework.exception;

import com.ggghost.framework.dto.ResponseInfo;
import com.ggghost.framework.enums.ExceptionEnum;
import com.ggghost.framework.exception.user.UserAuthenticationException;
import com.ggghost.framework.exception.user.UserLoginExpireException;
import com.ggghost.framework.exception.user.UserNotFoundException;
import com.ggghost.framework.exception.user.UserPasswordNotMatcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-17
 * @Description: 全局异常处理器
 * @Version: 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandle {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ResponseInfo<?> baseExceptionHandle(final BaseException e) {
        logger.error(e.getMessage(), e);
        return ResponseInfo.fail(ExceptionEnum.OPERATION_ERROR);
    }

    /**
     * 用户不存在
     * @param e
     * @return
     */
    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseBody
    public ResponseInfo<?> userNotFoundExceptionHandle(final UserNotFoundException e) {
        logger.error(e.getMsg(), e);
        return ResponseInfo.fail(ExceptionEnum.NO_USER_ERROR);
    }

    /**
     * 账号或密码不正确
     * @param e
     * @return
     */
    @ExceptionHandler(value = UserPasswordNotMatcherException.class)
    @ResponseBody
    public ResponseInfo<?> userPasswordNotMatcher(final UserPasswordNotMatcherException e) {
        return ResponseInfo.fail(ExceptionEnum.USERNAME_OR_PASSWORD_NOT_MATCHER);
    }

    @ExceptionHandler(value = UserLoginExpireException.class)
    @ResponseBody
    public ResponseInfo<?> userLoginExpire(final UserLoginExpireException e) {
        return ResponseInfo.fail(ExceptionEnum.USER_LOGIN_EXPIRE);
    }

    @ExceptionHandler(value = UserAuthenticationException.class)
    @ResponseBody
    public ResponseInfo<?> userAuthenticationException(final UserAuthenticationException e) {
        return ResponseInfo.fail(ExceptionEnum.USER_AUTHENTICATION_EXPIRE);
    }
}
