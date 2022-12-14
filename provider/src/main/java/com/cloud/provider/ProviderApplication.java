package com.cloud.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;

import static constants.Constants.MAPPERSCAN_BASEPACKAGES;

/**
 * api服务生产者
 */
@SpringBootApplication
@EnableHystrix
@MapperScan(MAPPERSCAN_BASEPACKAGES)
@EnableCaching
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean
    public Redisson getRedisson() {
        Config config = new Config();
        String addr = "http://" + redisHost + ":" + redisPort;
        config.useSingleServer().setAddress(addr);
        return (Redisson) Redisson.create(config);
    }

}
