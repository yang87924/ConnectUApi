package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Thread;
public interface IThreadService extends IService<Thread> {
    void selectPage(Integer page, Integer amount);
}
