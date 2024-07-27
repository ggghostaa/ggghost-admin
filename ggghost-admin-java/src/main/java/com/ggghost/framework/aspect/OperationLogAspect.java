package com.ggghost.framework.aspect;

import com.ggghost.framework.annotations.OperationLog;
import com.ggghost.framework.utlis.IpAddrUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.modeler.OperationInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.concurrent.*;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-18
 * @Description: 操作日志切面
 * @Version: 1.0
 */
@Aspect
@Component
public class OperationLogAspect {

    public static ExecutorService executorService = Executors.newCachedThreadPool();

    @Around("execution(* com.ggghost.framework.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //前置
        Instant start = Instant.now();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLog annotation = signature.getMethod().getAnnotation(OperationLog.class);
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        }
        return proceed;
    }

}
