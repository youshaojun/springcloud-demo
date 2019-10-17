package com.cloud.provider.service.impl;

import com.cloud.provider.dao.dao01.UserDao01;
import com.cloud.provider.dao.dao02.UserDao02;
import com.cloud.provider.entity.User;
import com.cloud.provider.service.TestDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TestDBServiceImpl implements TestDBService {

    @Autowired
    UserDao01 userDao01;
    @Autowired
    UserDao02 userDao02;

    public void testDB(User user) {
        userDao01.add(user);
        int a = 1 / 0;
        userDao02.add(user);
    }
}
