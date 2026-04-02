package com.Mike12138210.service;

import com.Mike12138210.pojo.User;

public interface UserService {
    boolean register(User user);

    User selectByAccount(String account);

    boolean updateDorm(String account, String building, String room);

    boolean updatePwd(String newPwd);
}