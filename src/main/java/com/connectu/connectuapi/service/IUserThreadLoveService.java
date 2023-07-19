package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.domain.UserThreadLove;
import com.github.yulichang.base.MPJBaseService;

public interface IUserThreadLoveService extends MPJBaseService<UserThreadLove> {
    int toggleLove(Integer userId, Integer threadId);
    String getLoveMessage(int userLoveStatus);
}
