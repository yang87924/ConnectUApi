package com.connectu.connectuapi.service.impl;

import com.connectu.connectuapi.dao.FavoriteDyThreadDao;
import com.connectu.connectuapi.dao.FavoriteThreadDao;
import com.connectu.connectuapi.domain.FavoriteDyThread;
import com.connectu.connectuapi.domain.FavoriteThread;
import com.connectu.connectuapi.service.IFavoriteDyThreadService;
import com.connectu.connectuapi.service.IFavoriteThreadService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class FavoriteDyThreadServiceImpl extends MPJBaseServiceImpl<FavoriteDyThreadDao, FavoriteDyThread> implements IFavoriteDyThreadService {
    private final FavoriteDyThreadDao favoriteDyThreadDao;
    public FavoriteDyThreadServiceImpl(FavoriteDyThreadDao favoriteDyThreadDao) {
        this.favoriteDyThreadDao = favoriteDyThreadDao;
    }
    @Override
    public void insertBatchRandomData(int count, int userIdStart, int userIdEnd, int dyThreadIdStart, int dyThreadIdEnd) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int randomUserId = random.nextInt(userIdEnd - userIdStart + 1) + userIdStart;
            int randomDyThreadId = random.nextInt(dyThreadIdEnd - dyThreadIdStart + 1) + dyThreadIdStart;
            FavoriteDyThread favoriteDyThread = new FavoriteDyThread();
            favoriteDyThread.setUserId(randomUserId);
            favoriteDyThread.setDyThreadId(randomDyThreadId);
            favoriteDyThreadDao.insert(favoriteDyThread);
        }
    }
}