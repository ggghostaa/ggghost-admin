package com.ggghost.framework.constant;

import java.time.Duration;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-25
 * @Description: redis常用参数
 * @Version: 1.0
 */
public class RedisConstant {
    public static final String LOCK = "G_LOCK_";
    public static final String USER = "G_USER_";
    public static final String JWT = "G_JWT_";
    public static final String JWT_USER = "G_JWT_USER_";
    public static final String JWT_LOGIN_USER = "G_JWT_USER_";
    public static final Duration JWT_DURATION = Duration.ofDays(7);
}
