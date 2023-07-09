package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.User;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface IUserService extends MPJBaseService<User> {
    User login(String account, String password);
    String getAvatarFromName(String userName);
    User selectGoogleUserByEmail(String email);
    User loginByGoogle(String token);
    void addFakeUsers(int count);
    List<User> getSortedUsers();



}
