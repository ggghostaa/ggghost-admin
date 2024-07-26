package com.ggghost.framework.service.impl;

import jakarta.annotation.Resource;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-24
 * @Description: reids service
 * @Version: 1.0
 */
@Service("redisService")
public class RedisService {
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    /**
     * 默认缓存时间
     */
    private static final Long DEFAULT_EXPIRED = 5 * 60L;
    /**
     * redis key前缀
     */
    private static final String REDIS_KEY_PREFIX = "";

    /**
     * 默认过期时长，单位：秒
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24;

    /**
     * 不设置过期时长
     */
    public static final long NOT_EXPIRE = -1;
    //等待锁时间
    private static final long WAIT_LOCK_TIME = 5000L;

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public <T>boolean put(String key, T value) {
        try {
            RBucket<Object> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
            bucket.set(value);
        } catch (Exception e) {
            log.error("redis set failed!\n\r error:{}",e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public <T>boolean put(String key, T value, Duration expire) {
        try {
            RBucket<Object> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
            bucket.set(value, expire);
        } catch (Exception e) {
            log.error("redis set failed!\n\r error:{}",e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * 以string方式读取
     * @param key
     * @return
     */
    public boolean putString(String key, String value) {
        try {
            RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key, StringCodec.INSTANCE);
            bucket.set(value);
        } catch (Exception e) {
            log.error("redis set failed!\n\r error:{}",e.getMessage());
            return false;
        }
        return true;
    }

    /**
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public boolean putString(String key, String value, Duration expire) {
        RBucket<String> bucket = null;
        try {
            bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key, StringCodec.INSTANCE);
            bucket.set(value, expire);
        } catch (Exception e) {
            log.error("redis set failed!\n\r error:{}",e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 以string方式读取
     * @param key
     * @return
     */
    public String getString(String key) {
        RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key, StringCodec.INSTANCE);
        return bucket.get();
    }

    /**
     * 获取值
     * @param key
     * @return
     */
    public <T> T get(String key) {
        RBucket<T> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
        return bucket.get();
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redissonClient.getBucket(REDIS_KEY_PREFIX + key).isExists();
    }

    /**
     * 如果不存在则写入
     * @param key
     * @param value
     * @param expired
     * @return
     */
    public boolean putStringIfAbsent(String key, String value, Duration expired) {
            RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
            return bucket.setIfAbsent(value, expired);
    }

    /**
     * 如果不存在则写入
     * @param key
     * @param value
     * @return
     */
    public boolean putStringIfAbsent(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
        return bucket.setIfAbsent(value);
    }

    /**
     * 删除key
     * @param key
     */
    public void remove(String key) {
        redissonClient.getBucket(REDIS_KEY_PREFIX + key).delete();
    }

    /**
     * 暴露redisson的list对象
     * @param key
     * @return
     * @param <T>
     */
    public <T> RList<T> getRList(String key) {
        return redissonClient.getList(REDIS_KEY_PREFIX + key);
    }

    /**
     * 暴露redisson的RMapCache对象
     * @param key
     * @return
     * @param <K>
     * @param <V>
     */
    public <K, V> RMapCache<K, V> getRMapCache(String key) {
        return redissonClient.getMapCache(REDIS_KEY_PREFIX + key);
    }

    /**
     * 暴露redisson的RSET对象
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> RSet<T> getRSet(String key) {
        return redissonClient.getSet(REDIS_KEY_PREFIX + key);
    }

    public void test() {
        RBuckets buckets = redissonClient.getBuckets();
    }

    /**
     * 重设过期时间
     * @param key
     * @param expire
     */
    public void setExpire(String key, Duration expire) {
        RBucket<Object> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
        bucket.expire(expire);
    }


    /**
     * 暴露redisson的RScoredSortedSet对象
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> RScoredSortedSet<T> getRScoredSortedSet(String key) {
        return redissonClient.getScoredSortedSet(REDIS_KEY_PREFIX + key);
    }


    /**
     * 暴露redisson的Lock对象
     *
     * @param key
     * @return
     */
    public RLock getRLock(String key) {
        return redissonClient.getLock(REDIS_KEY_PREFIX + key);
    }

}
