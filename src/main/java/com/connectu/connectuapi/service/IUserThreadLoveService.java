package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.domain.UserThreadLove;

public interface IUserThreadLoveService extends IService<UserThreadLove> {
    int toggleLove(Integer userId, Integer threadId);
    String getLoveMessage(int userLoveStatus);
}
