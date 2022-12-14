package com.cloud.provider.service.impl;


import com.cloud.provider.dao.dao03.UserDao03;
import com.cloud.provider.entity.User;
import com.cloud.provider.redis.RedisUtil;
import com.cloud.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static constants.Constants.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao03 userDao;
    @Autowired
    RedisUtil<User> redisUtil;


    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    // list 索引
    private AtomicInteger atomicInteger = new AtomicInteger(ZERO);

    public void add(User user) {
        int increment = atomicInteger.getAndIncrement();
        user.setIndex(increment);
        int i = userDao.add(user);
        if (i <= ZERO)
            throw new RuntimeException(ADD_ERROR);
        if (increment == ZERO)
            // 服务端重启, atomicInteger重置, 需要重新排序, 直接将list设置过期
            redisUtil.expire(REDIS_USER_LIST_KEY, ZERO);
        else {
            // 如果redis中没有数据, 就不添加进redis
            if (redisUtil.getSize(REDIS_USER_LIST_KEY) > ZERO)
                redisUtil.addList(REDIS_USER_LIST_KEY, user, TIME_OUT);
        }
    }

    public void modify(User user) {
        int i = userDao.modify(user);
        if (i <= ZERO)
            throw new RuntimeException(MODIFY_ERROR);
        if (redisUtil.getSize(REDIS_USER_LIST_KEY) > ZERO)
            redisUtil.modifyList(REDIS_USER_LIST_KEY, user.getIndex(), user);
    }

    public User findOne(Integer id, Integer index) {
        if (index != null) {
            User user = redisUtil.getIndex(REDIS_USER_LIST_KEY, index);
            if (user != null)
                return user;
        }
        return userDao.findOne(id);
    }

    public List<User> selectAll() {
        List<User> users = userDao.selectAll();
        redisUtil.addList(USERS_KEY, users, TIME_OUT);
        return users;
    }

    public Map selectByPage(Integer page, Integer size) {
        long startTime = System.currentTimeMillis();
        Map map = redisUtil.getListPage(REDIS_USER_LIST_KEY, page, size);
        if (map.get(TOTAL) == null) {
            // list过期重置index
            atomicInteger.set(ZERO);
            selectBysql(page, size, startTime, map);
            return map;
        }
        System.out.println("redis中有数据, 返回list分页.......");
        long endTimeByRedis = System.currentTimeMillis();
        System.out.println("使用redis缓存查询所消耗时间为 :  " + (endTimeByRedis - startTime) + "ms");
        return map;
    }

    public void selectBysql(Integer page, Integer size, long startTime, Map map) {
        List<User> users = userDao.selectAll();
        int total = users.size();
        map.put(TOTAL, total);
        users.forEach(user -> {
                    user.setIndex(atomicInteger.get());
                    redisUtil.addList(REDIS_USER_LIST_KEY, user, TIME_OUT);
                    atomicInteger.getAndIncrement();
                }
        );
        int end = page * size;
        if (end > total)
            map.put(ROWS, users.subList((page - ONE) * size, total));
        else {
            map.put(ROWS, users.subList((page - ONE) * size, end));
        }
        long endTimeBySQL = System.currentTimeMillis();
        if (atomicBoolean.get()) {
            System.err.println("redis中暂无数据, mybatis一级缓存有数据.......");
            System.err.println("使用mybatis一级缓存查询所消耗时间为 :  " + (endTimeBySQL - startTime) + "ms");
        } else {
            System.err.println("redis中暂无数据, mybatis一级缓存暂无数据, 进入mysql查询.......");
            System.err.println("使用mysql查询所消耗时间为 :  " + (endTimeBySQL - startTime) + "ms");
            atomicBoolean.set(true);
        }
    }

}
