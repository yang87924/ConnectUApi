package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.FavoriteThreadDao;
import com.connectu.connectuapi.domain.FavoriteThread;
import com.connectu.connectuapi.service.IFavoriteThreadService;

import org.springframework.stereotype.Service;

@Service
public class FavoriteThreadServiceImpl extends ServiceImpl<FavoriteThreadDao, FavoriteThread> implements IFavoriteThreadService {
}
