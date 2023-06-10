package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.DyReplyDao;
import com.connectu.connectuapi.dao.DyThreadDao;
import com.connectu.connectuapi.domain.DyReply;
import com.connectu.connectuapi.domain.DyThread;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IDyThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;

@Service
public class DyThreadServiceImpl extends ServiceImpl<DyThreadDao, DyThread> implements IDyThreadService {
    @Autowired
    private DyThreadDao dythreadDao;
    @Autowired
    private DyReplyDao dyReplyDao;
    @Override
    public void addFakeDyThread(int count) {
        for (int i = 0; i < count; i++) {
            DyThread thread = DyThreadServiceImpl.createFakeDyThread(count);
            dythreadDao.insert(thread);
        }
    }

    @Override
    public boolean save(DyThread dyThread) {
        dyThread.setCreatedAt(getSystemTime());

        return super.save(dyThread);
    }
    public static DyThread createFakeDyThread(int count) {
        DyThread dythread = new DyThread();
        dythread.setUserId((int) (Math.random() * count) + 1);
        dythread.setTitle(generateFakeArticle(10));
        dythread.setContent(generateFakeArticle(100));
        dythread.setCreatedAt(getSystemTime());
        return dythread;
    }

    @Override
    public boolean removeById(Serializable id) {
        LambdaQueryWrapper<DyReply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DyReply::getDyThreadId, id);
        List<DyReply> result = dyReplyDao.selectList(lqw);
        for (DyReply Dyreply : result) {
            dyReplyDao.deleteById(Dyreply.getDyReplyId()) ;
        }
        return super.removeById(id);
    }


}
