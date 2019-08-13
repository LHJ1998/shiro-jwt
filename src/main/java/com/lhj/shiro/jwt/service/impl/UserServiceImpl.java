package com.lhj.shiro.jwt.service.impl;

import com.lhj.shiro.jwt.dao.UserMapper;
import com.lhj.shiro.jwt.pojo.User;
import com.lhj.shiro.jwt.pojo.UserExample;
import com.lhj.shiro.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);
        if(users != null && users.size() > 0){
            return users.get(0);
        }
        return null;
    }

    @Override
    public boolean saveUser(User user) {
        User dbUser = getUserByUsername(user.getUsername());
        if(dbUser != null){
            return false;
        }
        user.setState(1);
        userMapper.insertSelective(user);
        return true;
    }
}
