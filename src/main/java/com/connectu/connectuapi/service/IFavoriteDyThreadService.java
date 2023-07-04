package com.connectu.connectuapi.service;

import com.connectu.connectuapi.domain.FavoriteDyThread;
import com.connectu.connectuapi.domain.FavoriteThread;
import com.github.yulichang.base.MPJBaseService;


public interface IFavoriteDyThreadService extends MPJBaseService<FavoriteDyThread> {
    void insertBatchRandomData(int count, int userIdStart, int userIdEnd, int dyThreadIdStart, int dyThreadIdEnd);

}
