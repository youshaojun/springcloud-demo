package com.cloud.provider.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cloud.provider.cache.ClassUtil.getClasses;


@Configuration
public class RedisConfig implements Serializable {

    @Value("${myCache.separator}")
    private String separator;
    @Value("${myCache.packageName}")
    private String packageName;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                this.getRedisCacheConfigurationWithTtl(60), // 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationMap() // 指定 key 策略
        );
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        validAnnotation(getClasses(packageName), redisCacheConfigurationMap);
        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));
        return redisCacheConfiguration;
    }

    /**
     * 自定义缓存失效时间
     */
    private void validAnnotation(List<Class<?>> clsList, Map<String, RedisCacheConfiguration> redisCacheConfigurationMap) {
        if (clsList != null && clsList.size() > 0) {
            clsList.forEach(cls -> {
                //获取类中的所有的方法
                Method[] methods = cls.getDeclaredMethods();
                if (methods != null && methods.length > 0) {
                    for (Method method : methods) {
                        Cacheable cacheable = method.getAnnotation(Cacheable.class);
                        if (cacheable != null) {
                            for (String v : cacheable.value()) {
                                String[] split = v.split(separator);
                                if (split.length == 2)
                                    redisCacheConfigurationMap.put(v, this.getRedisCacheConfigurationWithTtl(Integer.valueOf(split[1])));
                            }
                        }
                    }
                }
            });
        }
    }

}
