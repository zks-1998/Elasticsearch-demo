package com.gzhu.service;

import com.gzhu.mapper.UserMapper;
import com.gzhu.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findById(Long id){
        return userMapper.findById(id);
    }
}
