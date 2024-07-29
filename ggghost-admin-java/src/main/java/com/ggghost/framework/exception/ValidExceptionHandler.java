package com.ggghost.framework.exception;

import com.ggghost.framework.dto.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 校验异常处理
 */
@ControllerAdvice
public class ValidExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ValidExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseInfo<?> validExceptionHandle(BindException exception) {
        return getResponseInfo(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseInfo<?> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException exception) {
        return getResponseInfo(exception);
    }
    private ResponseInfo<?> getResponseInfo(BindException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(",");
        }
        String msg = sb.toString();
        log.error(msg);
        return ResponseInfo.fail(msg);
    }
}
