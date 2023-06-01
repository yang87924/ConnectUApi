package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.DyReplyDao;
import com.connectu.connectuapi.dao.impl.DyReply;
import com.connectu.connectuapi.service.IDyReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;
@Service
public class DyReplyServiceImpl extends ServiceImpl<DyReplyDao, DyReply>implements IDyReplyService {
    @Autowired
    private DyReplyDao dyReplyDao;
    @Override
    public void addFakeDyReply(int count) {
        for (int i = 0; i < count; i++) {
            DyReply dyReply = DyReplyServiceImpl.createFakeDyReplyd(count);
            dyReplyDao.insert(dyReply);
        }
    }
    public static DyReply createFakeDyReplyd(int count) {
        DyReply dyReply = new DyReply();
        dyReply.setDyThreadId((int) (Math.random() * count) + 1);
        dyReply.setUserId((int) (Math.random() * count) + 1);
        dyReply.setContent(generateFakeArticle(100));
        dyReply.setCreatedAt(getSystemTime());
        return dyReply;
    }
}
