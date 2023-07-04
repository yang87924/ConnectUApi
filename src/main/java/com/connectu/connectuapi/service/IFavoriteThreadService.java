package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.FavoriteThread;
import com.github.yulichang.base.MPJBaseService;


public interface IFavoriteThreadService extends MPJBaseService<FavoriteThread> {
    void insertBatchRandomData(int count, int userIdStart, int userIdEnd, int threadIdStart, int threadIdEnd);

}
