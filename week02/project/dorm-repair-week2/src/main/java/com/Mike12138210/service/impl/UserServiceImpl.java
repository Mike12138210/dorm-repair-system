package com.Mike12138210.service.impl;

import com.Mike12138210.mapper.UserMapper;
import com.Mike12138210.pojo.User;
import com.Mike12138210.service.UserService;
import com.Mike12138210.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(User user) {
        User existUser = userMapper.selectByAccount(user.getAccount());
        if(existUser != null){
            return false;
        }

        int rows = userMapper.insertUser(user);
        return rows > 0;
    }

    @Override
    public User selectByAccount(String account){
        return userMapper.selectByAccount(account);
    }

    @Override
    public boolean updateDorm(String account, String building, String room){
        User user = userMapper.selectByAccount(account);
        if(user == null){
            return false;
        }

        if(building.equals(user.getBuilding()) && room.equals(user.getRoom())){
            return true;
        }

        int rows = userMapper.updateDorm(account,building,room);
        return rows > 0;
    }

    @Override
    public boolean updatePwd(String newPwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        if(id == null){
            throw new RuntimeException("未登录或token无效");
        }
        int rows = userMapper.updatePwd(newPwd,id);
        return  rows > 0;
    }
}