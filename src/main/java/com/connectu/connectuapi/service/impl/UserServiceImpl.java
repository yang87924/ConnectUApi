package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.dao.impl.User;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<Integer> getAllUserId() {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.select("userId "); // 指定要查询的字段

        List<User> userList = userDao.selectList(queryWrapper);

        List<Integer> userId = new ArrayList<>();
        for (User user : userList) {
            userId.add(user.getUserId());
        }
        return userId;
    }
}
