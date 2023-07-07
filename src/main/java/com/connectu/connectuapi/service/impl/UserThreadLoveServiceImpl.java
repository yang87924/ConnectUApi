package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.UserThreadLoveDao;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.UserThreadLove;
import com.connectu.connectuapi.service.IThreadService;
import com.connectu.connectuapi.service.IUserThreadLoveService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class UserThreadLoveServiceImpl extends MPJBaseServiceImpl<UserThreadLoveDao, UserThreadLove> implements IUserThreadLoveService {
    @Autowired
    private UserThreadLoveDao userThreadLoveDao;
    @Autowired
    private IThreadService threadService;
    @Override
    public int toggleLove(Integer userId, Integer threadId) {
        // 查找用戶對文章的按讚記錄，包括邏輯刪除的記錄
        QueryWrapper<UserThreadLove> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId).eq("threadId", threadId).eq("deleted", 1);
        UserThreadLove userThreadLove = userThreadLoveDao.selectOne(queryWrapper);
        // 如果找不到記錄，創建一個新的按讚記錄
        if (userThreadLove == null) {
            userThreadLove = new UserThreadLove();
            userThreadLove.setUserId(userId);
            userThreadLove.setThreadId(threadId);
            userThreadLove.setLoveStatus(1);
            userThreadLoveDao.insert(userThreadLove);
        } else {
            // 切換按讚狀態
            userThreadLove.toggleLove();
            userThreadLoveDao.updateById(userThreadLove);
        }
        // 更新文章的按讚數
        Thread thread = threadService.getById(threadId);
        if (userThreadLove.getLoveStatus() == 1) {
            thread.setLove(thread.getLove() + 1);
        } else {
            thread.setLove(thread.getLove() - 1);
            userThreadLoveDao.deleteById(userThreadLove.getUserThreadLoveId()); // 刪除按讚記錄
        }
        threadService.updateById(thread);
        // 更新loveStatus的值
        thread.updateLoveStatus(userThreadLove.getLoveStatus());
        threadService.updateById(thread);
        // 返回當前用戶的按讚狀態
        return userThreadLove.getLoveStatus();
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