package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.User;

public interface IUserService extends IService<User> {
    User login(String account, String password);

    User selectGoogleUserByEmail(String email);
    User loginByGoogle(String token);
    void addFakeUsers(int count);


}
