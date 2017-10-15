package com.huilianjk.bonus.service;

import com.huilianjk.bonus.dao.UserMapper;
import com.huilianjk.bonus.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by space on 2017/10/15.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void insert(User user) {
        userMapper.insert(user);
    }

    public User getUserByOpenId(String openId) {
        return userMapper.getUserByOpenId(openId);
    }
}
