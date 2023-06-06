package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.controller.Code;
import com.connectu.connectuapi.controller.Result;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.exception.BusinessException;
import com.connectu.connectuapi.exception.SystemException;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getById(Serializable id) {
        if((Integer)id==1) {
            throw new BusinessException(Code.BUSINESS_ERR, "id==1 exception");
        }
        return super.getById(id);
    }
}
