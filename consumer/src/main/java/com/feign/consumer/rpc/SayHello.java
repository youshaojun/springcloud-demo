package com.feign.consumer.rpc;

import com.feign.consumer.rpc.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * cloud-provider 要调用服务的别名
 * configuration 配置类
 */
@FeignClient(value = "cloud-provider", configuration = FeignConfig.class)
@Component
public interface SayHello {

    //调用生产者的接口地址
    @RequestMapping("/test/provider")
    String sayHello();
}
