package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.exception.*;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User login(String account, String password) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getEmail, account);
        List<User> result = userDao.selectList(qw);
        if (result==null||result.isEmpty()) {
            throw new UserNotFoundException();
        } else if (result.get(0).getPassword().equals(password)) {
            return result.get(0);
        } else {
            throw new PasswordNotMatchException();
        }
    }


    @Override
    public boolean save(User newUser) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, newUser.getEmail());
        List<User> result = userDao.selectList(lqw);
        if(newUser.getUserName()==null||newUser.getEmail()==null||newUser.getPassword()==null
        ||newUser.getUserName().isEmpty()||newUser.getEmail().isEmpty()||newUser.getPassword().isEmpty()) {
            throw new ColumnIsNullException();
        }
        if(!newUser.getEmail().contains("@")){
            throw new EmailFormNotMatchException();
        }
        if(!result.isEmpty()){
            System.out.println(result);
            throw new EmailDuplicateException();
        }
        if(newUser.getPassword().length()<6||newUser.getPassword().length()>15){
            throw new PasswordFormNotMatchException();
        }
        return super.save(newUser);
    }
}
