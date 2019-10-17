package com.cloud.provider.controller;

import com.cloud.provider.entity.User;
import com.cloud.provider.service.TestDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import result.Result;

import static result.Result.SUCCESS;

/**
 * atomikos解决多数据源分布式事务问题
 */
@RestController
public class TestDBController {

    @Autowired
    TestDBService testDBService;

    @RequestMapping(value = "/testDB")
    public Result add(User user) {
        user.setAddress("xxxx");
        user.setUsername("oooo");
        testDBService.testDB(user);
        return SUCCESS();
    }
}
