package com.cloud.provider.redis;


import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static constants.Constants.*;


@Component
public class RedissonUtil {

    @Autowired
    Redisson redisson;


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
