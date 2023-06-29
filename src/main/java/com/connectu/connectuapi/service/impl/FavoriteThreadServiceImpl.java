package com.connectu.connectuapi.service.impl;

import com.connectu.connectuapi.dao.FavoriteThreadDao;
import com.connectu.connectuapi.domain.FavoriteThread;
import com.connectu.connectuapi.service.IFavoriteThreadService;
import com.github.yulichang.base.MPJBaseService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FavoriteThreadServiceImpl extends MPJBaseServiceImpl<FavoriteThreadDao, FavoriteThread> implements IFavoriteThreadService {
}
