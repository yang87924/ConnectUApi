package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.ReplyDao;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IReplyService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.connectu.connectuapi.service.utils.faker.*;

@Service
@Transactional
public class ReplyServiceImpl extends MPJBaseServiceImpl<ReplyDao, Reply> implements IReplyService {
    @Autowired
    private ReplyDao replyDao;
    @Override
    public void addFakeReply(int count) {
        for (int i = 0; i < count; i++) {
            Reply fakeReply = ReplyServiceImpl.createFakeReply(count);
            replyDao.insert(fakeReply);
        }
    }
    public static Reply createFakeReply(int count) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        String formattedDate = dateFormat.format(currentDate);
        Reply Reply = new Reply();
        Reply.setThreadId((int) (Math.random() * 71) + 1);
        Reply.setUserId((int) (Math.random() * 98) + 1);
        Reply.setContent(generateRandomString(50,200));
        Reply.setCreatedAt(formattedDate);
        return Reply;
    }
    @Override
    public boolean save(Reply reply) {
        reply.setCreatedAt(getSystemTime());

        return super.save(reply);
    }
    public List<Reply> getThreadReplyById(int id) {
        LambdaQueryWrapper<Reply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Reply::getThreadId, id);
        List<Reply> result = replyDao.selectList(lqw);
        return result;
    }
}
