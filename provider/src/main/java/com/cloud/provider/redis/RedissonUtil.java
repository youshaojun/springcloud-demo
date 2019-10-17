package com.cloud.provider.redis;


import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonUtil {

    @Autowired
    Redisson redisson;
    // 拼接key前缀
    private static final String LOCK_TITLE = "redisLock_";
    // 获取锁的等待时间, 若在此时间内没有获取到则直接返回
    private static final long WAIT_TIME = 1L;
    // 锁失效时间
    private static final long TIME_OUT = 10L;
    // 时间单位
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    // 获取锁 + 等待时间 + 超时时间
    public boolean tryLock(String lockName) throws InterruptedException {
        // 声明key对象
        String key = LOCK_TITLE + lockName;
        // 获取锁对象
        RLock lock = redisson.getLock(key);
        boolean lockStatus = lock.tryLock(WAIT_TIME, TIME_OUT, TIME_UNIT);
        System.err.println("======lock======" + Thread.currentThread().getName());
        return lockStatus;
    }

    // 锁的释放
    public void unLock(String lockName) {
        // 必须是和加锁时的同一个key
        String key = LOCK_TITLE + lockName;
        // 获取所对象
        RLock lock = redisson.getLock(key);
        // 释放锁（解锁）
        lock.unlock();
        System.err.println("======unlock======" + Thread.currentThread().getName());
    }

}
