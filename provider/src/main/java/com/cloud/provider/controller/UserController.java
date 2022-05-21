package com.cloud.provider.controller;


import com.cloud.provider.entity.User;
import com.cloud.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import result.Result;
import result.ResultData;

import static result.Result.SUCCESS;
import static result.ResultData.SUCCESS;

/**
 * redis 实现分页缓存
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/selectAll")
    @Cacheable(value = "userSelectAllCache#3600")
    public ResultData selectAll() {
        return SUCCESS(userService.selectAll());
    }

    @RequestMapping("/selectByPage")
    @Cacheable(key = "#page + '_'+ #size", value = "userSelectByPageCache#600")
    public ResultData selectByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return SUCCESS(userService.selectByPage(page, size));
    }

    @RequestMapping("/add")
    public Result add(User user) {
        userService.add(user);
        return SUCCESS();
    }

    @RequestMapping("/modify")
    public Result modify(User user) {
        userService.modify(user);
        return SUCCESS();
    }

    @RequestMapping("/findOne")
    public ResultData findOne(Integer id, Integer index) {
        return SUCCESS(userService.findOne(id, index));
    }


}
