package com.cloud.provider.dao.dao03;

import com.cloud.provider.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao03 {

    int add(User user);

    int modify(User user);

    User findOne(Integer id);

    List<User> selectAll();

}
