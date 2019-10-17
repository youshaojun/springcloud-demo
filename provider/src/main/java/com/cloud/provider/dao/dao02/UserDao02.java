package com.cloud.provider.dao.dao02;

import com.cloud.provider.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao02 {

    int add(User user);
}