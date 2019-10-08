package com.cloud.provider.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/test")
@Slf4j
public class CloudProviderController {

    @Value("${server.port}")
    String port;

    private static final String FALL_BACK_MSG = "服务器开小差啦";

    @RequestMapping("/provider")
    @HystrixCommand(fallbackMethod = "fallback")
    public String sayHello() throws InterruptedException, UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        log.info("当前服务器地址: " + address.getHostAddress());
        log.info("当前服务端口号: " + port);
        log.info("当前线程名称: " + Thread.currentThread().getName());
        log.info("接口被调用啦!!!!!");
        //Thread.sleep(6000);
        return "hello world";
    }

    public String fallback() {
        log.info("进入服务熔断 ==>> " + FALL_BACK_MSG);
        return FALL_BACK_MSG;
    }

}
