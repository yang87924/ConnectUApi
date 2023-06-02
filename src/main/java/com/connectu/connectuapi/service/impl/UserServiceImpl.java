package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.dao.impl.User;
import com.connectu.connectuapi.service.IUserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Locale;
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {
    @Autowired
    private UserDao userDao;
}
