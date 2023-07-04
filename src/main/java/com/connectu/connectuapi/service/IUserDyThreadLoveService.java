package com.connectu.connectuapi.service;

import com.connectu.connectuapi.domain.UserDyThreadLove;
import com.connectu.connectuapi.domain.UserThreadLove;
import com.github.yulichang.base.MPJBaseService;

public interface IUserDyThreadLoveService extends MPJBaseService<UserDyThreadLove> {
    int toggleLove(Integer userId, Integer threadId);
    String getLoveMessage(int userLoveStatus);
}
