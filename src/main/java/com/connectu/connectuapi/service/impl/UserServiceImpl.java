package com.connectu.connectuapi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.exception.PasswordNotMatchException;
import com.connectu.connectuapi.exception.UserNotFoundException;
import com.connectu.connectuapi.service.IUserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {
    @Autowired
    private UserDao userDao;
    public void addFakeUsers(int count) {
        for (int i = 0; i < count; i++) {
            User fakeUser = UserServiceImpl.createFakeUser();
            userDao.insert(fakeUser);
        }
    }
    public static User createFakeUser() {
        Faker faker = new Faker(new Locale("zh-CN"));
        User user = new User();
        String username = faker.internet().password();
        user.setEmail(faker.internet().emailAddress(username));
        user.setPassword(faker.internet().password());
        user.setUserName(faker.name().name());
        return user;
    }
    @Override
    public User login(String account, String password) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getEmail, account).or().eq(User::getUserName, account);
        List<User> result = userDao.selectList(qw);
        if (result==null||result.isEmpty()) {
            throw new UserNotFoundException();
        } else if (result.get(0).getPassword().equals(password)) {
            return result.get(0);
        } else {
            throw new PasswordNotMatchException();
        }
    }

    public boolean createAccount(User newUser){


        return false;
    }


}
