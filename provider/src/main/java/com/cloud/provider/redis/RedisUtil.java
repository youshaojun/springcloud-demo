package com.cloud.provider.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static constants.Constants.*;

@Component
public class RedisUtil<T> {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    /**
     * 删除指定key
     *
     * @param key
     */

    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 删除Obj缓存
     *
     * @param o
     */
    public void delObj(Object o) {
        redisTemplate.delete(o);
    }

    /**
     * 添加对象到redis 里面的list中
     * redis中的 list 是双向的 所以添加的时候需要注意
     * rightPush 先进先出 leftPush 先进后出 这里 需要注意
     *
     * @param key     list 对应的key
     * @param obj     需要存的对象
     * @param timeOut 过期时间
     */
    public void addList(String key, Object obj, long timeOut) {
        redisTemplate.opsForList().rightPush(key, obj);
        // 这里设置过期时间不是原子操作, 存在失败的风险
        expire(key, timeOut);
    }

    /**
     * opsForList().range(key, start, end);  取范围值  redis里面的list下标从0开始
     * 流程 拿到key 对应的list 取 0 到 5  和 mysql的limt  类似 注意下标即可
     * 从redis list 里面的获取数据分页
     *
     * @param key   redis list 对应的key
     * @param start 开始下标
     * @param end   介绍下标
     * @return 返回list给前端
     */
    public Map getListPage(String key, int start, int end) {
        Map map = new HashMap();
        Long size = redisTemplate.opsForList().size(key);
        map.put(TOTAL, size == ZERO ? null : size);
        map.put(ROWS, redisTemplate.opsForList().range(key, (start - ONE) * end, start * end - ONE));
        return map;
    }

    /**
     * 修改指定下标数据
     *
     * @param key
     * @param index
     * @param value
     */
    public void modifyList(String key, int index, T value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 根据下标获取指定数据
     *
     * @param key
     * @param index
     * @return
     */
    public T getIndex(String key, int index) {
        return (T) redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取list长度
     *
     * @param key
     * @return
     */
    public long getSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 失效
     *
     * @param key
     * @param timeOut
     */
    public void expire(String key, long timeOut) {
        redisTemplate.expire(key, timeOut, TIME_UNIT);
    }

}
