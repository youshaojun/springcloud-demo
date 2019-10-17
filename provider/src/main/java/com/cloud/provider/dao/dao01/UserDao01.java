package com.cloud.provider.dao.dao01;

import com.cloud.provider.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao01 {

    int add(User user);
}