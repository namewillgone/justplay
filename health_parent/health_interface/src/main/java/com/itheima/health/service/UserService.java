package com.itheima.health.service;

import com.itheima.health.pojo.User;

public interface UserService {

    User findUserByUsername(String username);
}
