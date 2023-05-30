package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.dao.impl.User;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User>implements IUserService {
    @Autowired
    private UserDao userDao;
    public void addFakeUsers(int count) {
        for (int i = 0; i < count; i++) {
            User fakeUser = User.createFakeUser();
            userDao.insert(fakeUser);
        }
    }
}
