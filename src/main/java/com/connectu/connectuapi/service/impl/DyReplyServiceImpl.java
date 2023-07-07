package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.DyReplyDao;
import com.connectu.connectuapi.domain.DyReply;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.service.IDyReplyService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;

//import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
@Service
@Transactional
public class DyReplyServiceImpl extends MPJBaseServiceImpl<DyReplyDao, DyReply> implements IDyReplyService {
    @Autowired
    private DyReplyDao dyReplyDao;
    @Override
    public void addFakeDyReply(int count) {
        for (int i = 0; i < count; i++) {
            DyReply dyReply = DyReplyServiceImpl.createFakeDyReplyd(count);
            dyReplyDao.insert(dyReply);
        }
    }
    @Override
    public boolean save(DyReply dyreply) {
        dyreply.setCreatedAt(getSystemTime());

        return super.save(dyreply);
    }
    public static DyReply createFakeDyReplyd(int count) {
        DyReply dyReply = new DyReply();
        dyReply.setDyThreadId((int) (Math.random() * count) + 1);
        dyReply.setUserId((int) (Math.random() * count) + 1);
        dyReply.setContent(generateFakeArticle(100));
        dyReply.setCreatedAt(getSystemTime());
        return dyReply;
    }
    public List<DyReply> getDyThreadReplyById(int id) {
        LambdaQueryWrapper<DyReply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DyReply::getDyThreadId, id);
        List<DyReply> result = dyReplyDao.selectList(lqw);
        return result;
    }
}
