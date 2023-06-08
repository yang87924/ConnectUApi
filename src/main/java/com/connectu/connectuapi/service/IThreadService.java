package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Thread;
public interface IThreadService extends IService<Thread> {
    void addFakeThread(int count);
    //void PostThread(int categoryId,int userId,String title,String content,String picture)
}
