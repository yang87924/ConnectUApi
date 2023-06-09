package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.DyReply;
import com.connectu.connectuapi.domain.DyThread;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;

import java.util.List;

public interface IDyThreadService extends IService<DyThread> {
    void addFakeDyThread(int count);
    List<DyThread> getUserDyThreadById(int id);

}
