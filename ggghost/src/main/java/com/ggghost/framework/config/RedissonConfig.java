package com.ggghost.framework.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-24
 * @Description: redission config
 * @Version: 1.0
 */
@Configuration
public class RedissonConfig {
    private static final Logger log = LoggerFactory.getLogger(RedissonConfig.class);
    /**
     * redis host
     */
    @Value("${spring.data.redis.host}")
    private String host;
    /**
     * redis password
     */
    @Value("${spring.data.redis.password}")
    private String password;

    /**
     * redis port
     */
    @Value("${spring.data.redis.port}")
    private Integer port;
    /**
     * 连接超时时间
     */
    private Integer connectTimeout = 3000;


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+ host+":"+port)
                .setPassword(password)
                .setDatabase(0)
                .setTimeout(5000)
                .setSubscriptionConnectionMinimumIdleSize(1)
                .setSubscriptionConnectionPoolSize(256)
                .setConnectTimeout(connectTimeout)
                .setConnectionPoolSize(256)
                .setConnectionMinimumIdleSize(1)
                .setRetryAttempts(3)
                .setRetryInterval(3000)
                .setIdleConnectionTimeout(30000);
        config.setCodec(new JackJsonCodec());
        try {
            RedissonClient redissonClient = Redisson.create(config);
            log.info("redis connect success, server={}", host);
            return redissonClient;
        } catch (Exception e) {
            log.warn("redis repeat connect, config={}", config);
            throw new RuntimeException(e);
        }
    }
}
