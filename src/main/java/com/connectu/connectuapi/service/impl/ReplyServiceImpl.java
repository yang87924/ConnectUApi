package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.ReplyDao;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.service.IReplyService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static com.connectu.connectuapi.service.utils.faker.*;

@Service
@Transactional
public class ReplyServiceImpl extends MPJBaseServiceImpl<ReplyDao, Reply> implements IReplyService {
    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private UserDao userDao;
    public List<Reply> getThreadReplyById(int threadId) {
        LambdaQueryWrapper<Reply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Reply::getThreadId, threadId);
        List<Reply> result = replyDao.selectList(lqw);
        // 获取所有的userId
        List<Integer> userIdList = result.stream()
                .map(Reply::getUserId)
                .collect(Collectors.toList());
        // 根据userId查询对应的User数据
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.in(User::getUserId, userIdList);
        List<User> userList = userDao.selectList(userQueryWrapper);
        // 将User对象设置到Reply的user属性中
        Map<Integer, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getUserId, user -> user));
        for (Reply reply : result) {
            Integer userId = reply.getUserId();
            if (userMap.containsKey(userId)) {
               // reply.setUser(userDao.selectById((Serializable) userMap.get(userId)));
                reply.setUser(userDao.selectById(reply.getUserId()));
            }
        }
        return result;
    }
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

}
