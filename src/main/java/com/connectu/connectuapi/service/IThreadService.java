package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.dao.impl.Thread;

public interface IThreadService extends IService<Thread> {
    void addFakeThread(int count);
}
