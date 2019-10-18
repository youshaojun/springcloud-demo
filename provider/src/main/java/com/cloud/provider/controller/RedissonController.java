package com.cloud.provider.controller;

import com.cloud.provider.redis.RedissonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static constants.Constants.REDIS_KEY;

/**
 * redisson实现redis分布式锁
 */
@RestController
public class RedissonController {

    @Autowired
    RedissonUtil redissonUtil;

    @RequestMapping("/testRedisson")
    public String testRedisson() throws InterruptedException {

        // 获取锁
        if (redissonUtil.tryLock(REDIS_KEY))
            System.out.println("(testRedisson00000)获取锁成功......");
        else {
            System.out.println("(testRedisson00000)获取锁失败......");
            return "(testRedisson00000)获取锁失败......";
        }
        // 业务逻辑
        System.out.println("处理业务逻辑.............");
        Thread.sleep(15000);
        // 释放锁
        redissonUtil.unLock(REDIS_KEY);
        System.out.println("(testRedisson00000)释放锁成功......");
        return "(testRedisson00000)释放锁成功......";

    }

    @RequestMapping("/testRedisson1")
    public String testRedisson1() throws InterruptedException {
        if (redissonUtil.tryLock(REDIS_KEY)) {
            System.out.println("(testRedisson1111111)获取锁成功......");
            return "(testRedisson1111111)获取锁成功......";
        } else {
            System.out.println("(testRedisson1111111)获取锁失败......");
            return "(testRedisson1111111)获取锁失败......";
        }

    }


}
