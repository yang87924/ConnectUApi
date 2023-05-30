package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.ThreadDao;
import com.connectu.connectuapi.dao.impl.Thread;
import com.connectu.connectuapi.service.IThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ThreadServiceImpl extends ServiceImpl<ThreadDao, Thread> implements IThreadService {
    @Autowired
    private ThreadDao threadDao;
    @Override
    public void addFakeThread(int count) {
        for (int i = 0; i < count; i++) {
            Thread thread2 = Thread.createFakeThread();
            threadDao.insert(thread2);
        }
    }
}
