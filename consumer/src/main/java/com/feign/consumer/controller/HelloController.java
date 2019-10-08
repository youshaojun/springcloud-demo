package com.feign.consumer.controller;

import com.feign.consumer.rpc.SayHello;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@Slf4j
public class HelloController {

    @Value("${server.port}")
    String port;

    @Autowired
    SayHello sayHello;

    @RequestMapping("/test")
    public String test() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        log.info("当前服务器地址: " + address.getHostAddress());
        log.info("当前服务端口号: " + port);
        log.info("当前线程名称: " + Thread.currentThread().getName());
        log.info("接口被调用啦!!!!!");
        log.info("测试rabbitmq收集日志......");
        return sayHello.sayHello();
    }

}
