package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.ReplyDao;
import com.connectu.connectuapi.dao.ThreadDao;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;

@Service
public class ThreadServiceImpl extends ServiceImpl<ThreadDao, Thread> implements IThreadService {
    @Autowired
    private ThreadDao threadDao;
    @Autowired
    private ReplyDao replyDao;

    @Override
    public void addFakeThread(int count) {
        for (int i = 0; i < count; i++) {
            Thread thread = ThreadServiceImpl.createFakeThread(count);
            threadDao.insert(thread);
        }
    }
    public static Thread createFakeThread(int count) {
        Thread thread = new Thread();
        thread.setCategoryId((int) (Math.random() * 13) + 1);
        thread.setUserId((int) (Math.random() * count) + 1);
        thread.setTitle(generateFakeArticle(10));
        thread.setContent(generateFakeArticle(100));
        thread.setCreatedAt(getSystemTime());
        return thread;
    }
    @Override
    public boolean save(Thread thread) {
        thread.setCreatedAt(getSystemTime());

        return super.save(thread);
    }
    @Override
    public boolean removeById(Serializable id) {
        LambdaQueryWrapper<Reply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Reply::getThreadId, id);
        List<Reply> result = replyDao.selectList(lqw);
        for (Reply reply : result) {
            replyDao.deleteById(reply.getReplyId()) ;
        }
        return super.removeById(id);
    }

}
