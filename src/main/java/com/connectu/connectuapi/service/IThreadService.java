package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Thread;

import java.util.List;

public interface IThreadService extends IService<Thread> {
    void addFakeThread(int count);
}
