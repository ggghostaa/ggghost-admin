package com.ggghost.framework.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggghost.framework.custom.LoggingProperties;
import com.ggghost.framework.entity.log.ApiLog;
import com.ggghost.framework.repository.log.ApiLogRepository;
import com.ggghost.framework.utlis.IpAddrUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * api日志切面
 */
@Component
@Aspect
public class ApiLogAspect {

    private static final Logger log = LoggerFactory.getLogger(ApiLogAspect.class);
    @Autowired
    private LoggingProperties loggingProperties;
    @Autowired
    private ApiLogRepository apiLogRepository;

    private  final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public ExecutorService executorService = Executors.newCachedThreadPool();


    @Around(value = "execution(* com.ggghost.framework.controller..*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (shouldLog()) {
            return addLog(joinPoint);
        } else {
            return joinPoint.proceed();
        }
    }

    /**
     * 添加日志
     * @param joinPoint
     * @return
     */
    private Object addLog(ProceedingJoinPoint joinPoint) {
        Instant startTime = Instant.now();
        ZoneId zoneId = ZoneId.systemDefault();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        String ipAddress = IpAddrUtils.getIpAddress(request);
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("\n\r================ startTime:{}  uri:{} \n\r",
                startTime.atZone(zoneId).toLocalDateTime().toString()
        ,uri);

        String result = "";
        String requestParams = "";
        String errorMsg = "";
        try {
            Object proceed = joinPoint.proceed();
            result = objectMapper.writeValueAsString(proceed);
            requestParams = objectMapper.writeValueAsString(filterArgs(joinPoint.getArgs()));
            return proceed;
        } catch (Throwable e) {
            result = "request failed";
            errorMsg = e.getMessage();
            throw new RuntimeException(e);
        } finally {
            Instant endTime = Instant.now();
            Duration duration = Duration.between(startTime,endTime);
            log.info(
                            "\n\rrequest type:{} \n\r" +
                            "request method:{} \n\r" +
                            "request params:{} \n\r" +
                            "duration:{} ms \n\r" +
                            "request result:{} \n\r" +
                            "=================endTime:{}\n\r",
                    request.getMethod(),
                    joinPoint.getSignature(),
                    requestParams,
                    duration.toMillis(),
                    result,
                    endTime.atZone(zoneId).toLocalDateTime().toString()
                    );
            String finalResult = result;
            String finalRequestParams = requestParams;
            String finalErrorMsg = errorMsg;
            Instant now = Instant.now();
            //异步线程持久化
            Executors.newVirtualThreadPerTaskExecutor().execute(()->{
                ApiLog apiLog = new ApiLog();
                apiLog.setIp(ipAddress);
                apiLog.setIpInfo(IpAddrUtils.getIpInfo(ipAddress));
                apiLog.setApiUri(uri);
                apiLog.setApiUrl(url);
                apiLog.setBeginTime(startTime.atZone(zoneId).toLocalDateTime());
                apiLog.setEndTime(endTime.atZone(zoneId).toLocalDateTime());
                apiLog.setDuration(duration.toMillis());
                apiLog.setClassName(className);
                apiLog.setMethod(methodName);
                apiLog.setResponseParams(finalResult);
                apiLog.setRequestParams(finalRequestParams);
                apiLog.setErrMsg(finalErrorMsg);
                apiLogRepository.save(apiLog);
            });
        }
    }

    /**
     * 白名单判断是否打印该日志
     * @return
     */
    private boolean shouldLog() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        return loggingProperties.getExcludePaths().stream().noneMatch(api->pathMatcher.match(api, request.getRequestURI()));
    }


    /**
     * 打印参数过滤
     * @param args
     * @return
     */
    private List<Object> filterArgs(Object[] args) {
        return Arrays.stream(args).filter(object -> (!(object instanceof HttpServletRequest) &&
                !(object instanceof HttpServletResponse)) && !(object instanceof MultipartFile)
        ).collect(Collectors.toList());
    }
}
