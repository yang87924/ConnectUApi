package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.ThreadDao;
import com.connectu.connectuapi.dao.impl.Thread;
import com.connectu.connectuapi.service.IThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;
@Service
public class ThreadServiceImpl extends ServiceImpl<ThreadDao, Thread> implements IThreadService {
    @Autowired
    private ThreadDao threadDao;
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

}
