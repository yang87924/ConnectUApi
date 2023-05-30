package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.dao.impl.User;

public interface IUserService extends IService<User> {
    void addFakeUsers(int count);
}
