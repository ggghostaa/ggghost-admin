package com.ggghost.framework.common;

import java.time.Duration;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-24
 * @Description: 分布式锁
 * @Version: 1.0
 */
public interface DistributedLock {
    /**
     * 加锁
     * @return
     */
    boolean tryLock(String key);

    /**
     * 加锁
     * @return
     */
    boolean tryLock(String key, Duration duration);

    /**
     * 解锁
     * @param key
     * @return
     */
    boolean unlock(String key);
}
