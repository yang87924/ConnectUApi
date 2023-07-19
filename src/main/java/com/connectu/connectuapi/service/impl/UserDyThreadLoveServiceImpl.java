package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.connectu.connectuapi.dao.UserDyThreadLoveDao;
import com.connectu.connectuapi.dao.UserThreadLoveDao;
import com.connectu.connectuapi.domain.DyThread;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.UserDyThreadLove;
import com.connectu.connectuapi.domain.UserThreadLove;
import com.connectu.connectuapi.service.IDyThreadService;
import com.connectu.connectuapi.service.IThreadService;
import com.connectu.connectuapi.service.IUserDyThreadLoveService;
import com.connectu.connectuapi.service.IUserThreadLoveService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDyThreadLoveServiceImpl extends MPJBaseServiceImpl<UserDyThreadLoveDao, UserDyThreadLove> implements IUserDyThreadLoveService {
    @Autowired
    private UserDyThreadLoveDao userDyThreadLoveDao;
    @Autowired
    private IDyThreadService dyThreadService;
    @Autowired
    private IThreadService threadService;  // 注入 IThreadService
    @Override
    public int toggleLove(Integer userId, Integer dyThreadId) {
        // 查找用戶對文章的按讚記錄，包括邏輯刪除的記錄
        QueryWrapper<UserDyThreadLove> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId).eq("dyThreadId", dyThreadId).eq("deleted", 1);
        UserDyThreadLove userDyThreadLove = userDyThreadLoveDao.selectOne(queryWrapper);
        // 如果找不到記錄，創建一個新的按讚記錄
        if (userDyThreadLove == null) {
            userDyThreadLove = new UserDyThreadLove();
            userDyThreadLove.setUserId(userId);
            userDyThreadLove.setDyThreadId(dyThreadId);
            userDyThreadLove.setLoveStatus(1);
            userDyThreadLoveDao.insert(userDyThreadLove);
        } else {
            // 切換按讚狀態
            userDyThreadLove.toggleLove();
            userDyThreadLoveDao.updateById(userDyThreadLove);
        }
        // 更新文章的按讚數
        DyThread dythread = dyThreadService.getById(dyThreadId);
        if (userDyThreadLove.getLoveStatus() == 1) {
            dythread.setLove(dythread.getLove() + 1);
        } else {
            dythread.setLove(dythread.getLove() - 1);
            userDyThreadLoveDao.deleteById(userDyThreadLove.getUserDyThreadLoveId()); // 刪除按讚記錄
        }
        // 更新loveStatus的值
        dythread.updateLoveStatus(userDyThreadLove.getLoveStatus());
        dyThreadService.updateById(dythread);
        //threadService.up
        // 返回當前用戶的按讚狀態
        return userDyThreadLove.getLoveStatus();
    }
    @Override
    public String getLoveMessage(int userLoveStatus) {
        if (userLoveStatus == 1) {
            return "按讚成功";
        } else {
            return "收回讚成功";
        }
    }
}