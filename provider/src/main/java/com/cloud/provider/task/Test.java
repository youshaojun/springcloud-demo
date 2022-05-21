package com.cloud.provider.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Test {

    @Value("${IS_SYNC}")
    private boolean isSync;

    private static Lock lock = new ReentrantLock();

    public void aa() throws InterruptedException {
        if (!isSync) {
            boolean b = lock.tryLock();
            if (b) {
                aaMethod();
                Thread.sleep(120000);
                lock.unlock();
            }
        } else {
            aaMethod();
        }
    }

    public void aaMethod() {
        System.out.println(new Date() + " 执行 =====>>> aaMethod");
    }
}
