package com.cloud.provider.service;

import com.cloud.provider.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    void add(User user);

    void modify(User user);

    User findOne(Integer id, Integer index);

    List<User> selectAll();

    Map selectByPage(Integer page, Integer size);


}
