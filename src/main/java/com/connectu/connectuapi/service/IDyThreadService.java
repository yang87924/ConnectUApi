package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.DyThread;
public interface IDyThreadService extends IService<DyThread> {
    void addFakeDyThread(int count);
}
