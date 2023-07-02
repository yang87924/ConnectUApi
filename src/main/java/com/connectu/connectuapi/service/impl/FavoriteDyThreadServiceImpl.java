package com.connectu.connectuapi.service.impl;

import com.connectu.connectuapi.dao.FavoriteDyThreadDao;
import com.connectu.connectuapi.dao.FavoriteThreadDao;
import com.connectu.connectuapi.domain.FavoriteDyThread;
import com.connectu.connectuapi.domain.FavoriteThread;
import com.connectu.connectuapi.service.IFavoriteDyThreadService;
import com.connectu.connectuapi.service.IFavoriteThreadService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FavoriteDyThreadServiceImpl extends MPJBaseServiceImpl<FavoriteDyThreadDao, FavoriteDyThread> implements IFavoriteDyThreadService {
}
