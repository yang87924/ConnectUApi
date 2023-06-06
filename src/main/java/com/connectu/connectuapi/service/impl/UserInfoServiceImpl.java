package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.controller.Code;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.dao.UserInfoDao;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.domain.UserInfo;
import com.connectu.connectuapi.exception.BusinessException;
import com.connectu.connectuapi.service.IUserInfoService;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements IUserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

}
