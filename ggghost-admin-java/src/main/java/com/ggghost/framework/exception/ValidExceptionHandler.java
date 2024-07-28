package com.ggghost.framework.exception;

import com.ggghost.framework.dto.ResponseInfo;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 校验异常处理
 */
@RestControllerAdvice
public class ValidExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseInfo<?> validExceptionHandle(BindException exception) {
        return ResponseInfo.fail(exception.getMessage());
    }
}
