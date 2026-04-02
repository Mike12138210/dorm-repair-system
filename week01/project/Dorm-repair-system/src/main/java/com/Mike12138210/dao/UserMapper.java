package com.Mike12138210.dao;

import com.Mike12138210.pojo.User;

public interface UserMapper {
    // 插入用户（注册）
    int insertUser(User user);

    // 删除用户信息
    void deleteUser();

    // 修改用户信息（比如绑定宿舍、修改密码）
    int updateUser(User user);

    // 根据账号查询用户（用于登录）
    User selectByAccount(String account);
}