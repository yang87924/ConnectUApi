package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.User;

public interface IUserService extends IService<User> {
    User login(String account, String password);
    void addFakeUsers(int count);
}
