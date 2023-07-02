package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.DyReply;
import com.connectu.connectuapi.domain.DyThread;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface IDyThreadService extends MPJBaseService<DyThread> {
    void addFakeDyThread(int count);

    List<DyThread> getUserDyThreadById(int id);
    //切換使用者按讚--------------------------------------------------------------
    void toggleLove(DyThread dyThread);
    //新增收藏文章--------------------------------------------------------------
    boolean addFavoriteDyThread(Integer userId, Integer DyThreadId);
    void handleHashtags(DyThread dyThread, List<String> dyHashtags);
    //分頁查詢--------------------------------------------------------------
    IPage<DyThread> listWithPagination(Page<DyThread> page, Wrapper<DyThread> queryWrapper);
}
