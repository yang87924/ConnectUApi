package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.DyThreadDao;
import com.connectu.connectuapi.domain.DyThread;
import com.connectu.connectuapi.service.IDyThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;

@Service
public class DyThreadServiceImpl extends ServiceImpl<DyThreadDao, DyThread> implements IDyThreadService {
    @Autowired
    private DyThreadDao dythreadDao;
    @Override
    public void addFakeDyThread(int count) {
        for (int i = 0; i < count; i++) {
            DyThread thread = DyThreadServiceImpl.createFakeDyThread(count);
            dythreadDao.insert(thread);
        }
    }
    public static DyThread createFakeDyThread(int count) {
        DyThread dythread = new DyThread();
        dythread.setUserId((int) (Math.random() * count) + 1);
        dythread.setTitle(generateFakeArticle(10));
        dythread.setContent(generateFakeArticle(100));
        dythread.setCreatedAt(getSystemTime());
        return dythread;
    }
}
