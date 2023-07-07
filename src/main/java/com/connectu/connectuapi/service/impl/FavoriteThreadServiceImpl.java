package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.FavoriteThreadDao;
import com.connectu.connectuapi.domain.FavoriteThread;
import com.connectu.connectuapi.service.IFavoriteThreadService;

import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class FavoriteThreadServiceImpl extends MPJBaseServiceImpl<FavoriteThreadDao, FavoriteThread> implements IFavoriteThreadService {
    private final FavoriteThreadDao favoriteThreadDao;
    public FavoriteThreadServiceImpl(FavoriteThreadDao favoriteThreadDao) {
        this.favoriteThreadDao = favoriteThreadDao;
    }
    @Override
    public void insertBatchRandomData(int count, int userIdStart, int userIdEnd, int threadIdStart, int threadIdEnd) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int randomUserId = random.nextInt(userIdEnd - userIdStart + 1) + userIdStart;
            int randomThreadId = random.nextInt(threadIdEnd - threadIdStart + 1) + threadIdStart;
            FavoriteThread favoriteThread = new FavoriteThread();
            favoriteThread.setUserId(randomUserId);
            favoriteThread.setThreadId(randomThreadId);
            favoriteThreadDao.insert(favoriteThread);
        }
    }
}