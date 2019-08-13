package com.lhj.shiro.jwt.service;

import com.lhj.shiro.jwt.pojo.User;

public interface UserService {

    User getUserByUsername(String username);

    boolean saveUser(User user);

}
