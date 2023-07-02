package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.connectu.connectuapi.dao.*;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IDyReplyService;
import com.connectu.connectuapi.service.IDyThreadService;
import com.connectu.connectuapi.service.IFavoriteDyThreadService;
import com.connectu.connectuapi.service.IReplyService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;

@Service
public class DyThreadServiceImpl extends MPJBaseServiceImpl<DyThreadDao, DyThread> implements IDyThreadService {
    @Autowired
    private DyThreadDao dythreadDao;
    @Autowired
    private DyReplyDao dyReplyDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private IFavoriteDyThreadService favoriteDyThreadService;
    @Autowired
    private DyThreadHashtagDao dyThreadHashtagDao;
    @Autowired
    private DyHashtagDao dyHashtagDao;
    @Autowired
    private IDyReplyService dyReplyService;
    public void handleHashtags(DyThread dyThread, List<String> dyHashtags) {
        for (String dyHashtag : dyHashtags) {
            QueryWrapper<DyHashtag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", dyHashtag);
            DyHashtag existingHashtag = dyHashtagDao.selectOne(queryWrapper);
            if (existingHashtag != null) {
                existingHashtag.setAmount(existingHashtag.getAmount() + 1);
                dyHashtagDao.updateById(existingHashtag);
            } else {
                DyHashtag newDyHashtag = new DyHashtag();
                newDyHashtag.setName(dyHashtag);
                newDyHashtag.setAmount(1);
                dyHashtagDao.insert(newDyHashtag);
                existingHashtag = newDyHashtag;
            }
            dyThreadHashtag dyThreadHashtag = new dyThreadHashtag();
            dyThreadHashtag.setDyThreadHashtagId(dyThread.getDyThreadId());
            dyThreadHashtag.setDyHashtagId(existingHashtag.getDyHashtagId());
            dyThreadHashtagDao.insert(dyThreadHashtag);
        }
    }
    @Override
    public IPage<DyThread> listWithPagination(Page<DyThread> page, Wrapper<DyThread> queryWrapper) {
        IPage<DyThread> threadPage = super.page(page, queryWrapper);
        List<DyThread> dyThreads = threadPage.getRecords();
        // 批次查詢分類和使用者資訊

        List<Integer> userIds = dyThreads.stream().map(DyThread::getUserId).collect(Collectors.toList());
        Map<Integer, User> userMap = userDao.selectBatchIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
        for (DyThread dyThread : dyThreads) {

            User user = userMap.get(dyThread.getUserId());
            if (user != null) {
                dyThread.setUser(user);
            }
            dyThread.setReplyCount(dyReplyService.getDyThreadReplyById(dyThread.getDyThreadId()).size());
            // 批次查詢ThreadHashtag表
            List<dyThreadHashtag> dyThreadHashtags = dyThreadHashtagDao.selectList(new QueryWrapper<dyThreadHashtag>().eq("dyThreadId", dyThread.getDyThreadId()));
            List<Integer> hashtagIds = dyThreadHashtags.stream().map(dyThreadHashtag::getDyHashtagId).collect(Collectors.toList());
            if (!hashtagIds.isEmpty()) {
                List<DyHashtag> hashtags = dyHashtagDao.selectBatchIds(hashtagIds);
                dyThread.setHashtags(hashtags);
            }
        }
        return threadPage;
    }
    //假資料
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
       // dythread.setTitle(generateFakeArticle(10));
        dythread.setContent(generateFakeArticle(100));
        dythread.setCreatedAt(getSystemTime());
        return dythread;
    }
    //新增收藏文章--------------------------------------------------------------
    @Override
    public boolean addFavoriteDyThread(Integer userId, Integer dyThreadId) {
        //檢查使用者是否已經收藏過此文章
        QueryWrapper<FavoriteDyThread> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("dyThreadId", dyThreadId);
        FavoriteDyThread favoriteDyThread = favoriteDyThreadService.getOne(wrapper);
        if (favoriteDyThread != null) {
            //使用者已經收藏過此文章
            return true;
        }
        //新增收藏文章
        favoriteDyThread = new FavoriteDyThread();
        favoriteDyThread.setUserId(userId);
        favoriteDyThread.setDyThreadId(dyThreadId);
        boolean flag = favoriteDyThreadService.save(favoriteDyThread);
        if (flag) {
            //收藏文章數量+1
            DyThread dyThread = getById(dyThreadId);
            dyThread.setFavoriteCount(dyThread.getFavoriteCount() + 1);
            updateById(dyThread);
        }
        return flag;
    }
    //切換使用者按讚--------------------------------------------------------------
    @Override
    public void toggleLove(DyThread dyThread) {
        Integer love = dyThread.getLove();
        if (love % 2 == 0) {
            dyThread.setLove(love + 1);
        } else {
            dyThread.setLove(love - 1);
        }
    }
//    @Override
//    public boolean save(DyThread dyThread) {
//        dyThread.setCreatedAt(getSystemTime());
//        return super.save(dyThread);
//    }
    public List<DyThread> getUserDyThreadById(int id) {
        LambdaQueryWrapper<DyThread> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DyThread::getUserId, id);
        List<DyThread> result = dythreadDao.selectList(lqw);
        return result;
    }

}
